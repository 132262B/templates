package com.app;

import com.google.common.base.CaseFormat;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.metamodel.EntityType;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DatabaseCleanup implements InitializingBean {
    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tableNames;


    /**
     * InitializingBean 인터페이스를 구현하여 빈 초기화 후에 실행되는 메소드로, 엔티티 매니저를 사용하여
     * 현재 데이터베이스에 있는 모든 테이블의 이름을 수집합니다.
     * 테이블이름은 대문자 스네이크 케이스 또는 테이블 어노테이션에서 정의된 이름으로 작성됩니다.
     * 이 메소드는 execute() 메소드에서 사용됩니다.
     */
    @Override
    public void afterPropertiesSet() {
        final Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
        tableNames = entities.stream()
                .filter(e -> isEntity(e) && hasTableAnnotation(e))
                .map(e -> {
                    String tableName = e.getJavaType().getAnnotation(Table.class).name();
                    return tableName.isBlank() ? CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.getName()) : tableName;
                })
                .collect(Collectors.toList());

        final List<String> entityNames = entities.stream()
                .filter(e -> isEntity(e) && !hasTableAnnotation(e))
                .map(e -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.getName())).collect(Collectors.toList());

        tableNames.addAll(entityNames);
    }

    /**
     * 현재 데이터베이스에서 모든 테이블을 비우고 ID 값을 1부터 다시 시작하도록 재설정합니다.
     * 테이블 이름은 InitializingBean의 afterPropertiesSet() 메소드에서 수집된 테이블 이름 목록에서 가져옵니다.
     * 이 메소드는 트랜잭션 어노테이션이 적용된 execute() 메소드에서 호출됩니다.
     */
    @Transactional
    public void execute() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (final String tableName : tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN ID RESTART WITH 1").executeUpdate();
        }

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    /**
     * 주어진 엔티티가 JPA 엔티티인지 확인합니다.
     * @param e 엔티티
     * @return JPA 엔티티 여부
     */
    private boolean isEntity(final EntityType<?> e) {
        return null != e.getJavaType().getAnnotation(Entity.class);
    }

    private boolean hasTableAnnotation(final EntityType<?> e) {
        return null != e.getJavaType().getAnnotation(Table.class);
    }

}