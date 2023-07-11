# Hibernate/Spring Data JPA Bug: deleteAll() failing for entity with composite primary key

- _Please note that this has been logged as an issue in the Hibernate JIRA here: [https://github.com/philiplowney/deleteall-bug-spring-data-jpa](https://hibernate.atlassian.net/browse/HHH-16923)_
- _Please note also that this was originally reported here as an issue with Spring Data JPA: https://github.com/spring-projects/spring-data-jpa/issues/3065_

## Outline
We are unable to use the CrudRepository.deleteAll() method for an entity. We are using the following dependencies:
```
        <spring.version>6.0.10</spring.version>
        <spring-data-jpa.version>3.1.1</spring-data-jpa.version>
        <hibernate.version>6.2.6.Final</hibernate.version>
```

## Entity Model
Our Entity Model consists of the following:

      ┌───────────┐       ┌────┐
      │FormVersion│*─────1│Form│
      └───────────┘       └────┘
            1
            │
            │
            *
       ┌─────────┐
       │FormInput│
       └─────────┘

Note that `FormVersion` entities have a composite primary key consisting of the Form ID and a version number.

## Bug
When we create more than one `FormVersion` entity and try to delete them all by using the `CrudRepository.deleteAll()` 
method, we encounter the following error:
```agsl

org.springframework.transaction.TransactionSystemException: Could not commit JPA transaction

	at org.springframework.orm.jpa.JpaTransactionManager.doCommit(JpaTransactionManager.java:570)
	at org.springframework.transaction.support.AbstractPlatformTransactionManager.processCommit(AbstractPlatformTransactionManager.java:743)
	at org.springframework.transaction.support.AbstractPlatformTransactionManager.commit(AbstractPlatformTransactionManager.java:711)
	at org.springframework.transaction.interceptor.TransactionAspectSupport.commitTransactionAfterReturning(TransactionAspectSupport.java:660)
	at org.springframework.transaction.interceptor.TransactionAspectSupport.invokeWithinTransaction(TransactionAspectSupport.java:410)
	at org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:119)
	... omitted for brevity ...
Caused by: jakarta.persistence.RollbackException: Error while committing the transaction
	... omitted for brevity ...
Caused by: java.lang.NullPointerException: Cannot invoke java.util.Comparator.compare(Object, Object) because the return value of org.hibernate.type.descriptor.java.JavaType.getComparator() is null
	at org.hibernate.metamodel.mapping.AttributeMapping.compare(AttributeMapping.java:91)
	at org.hibernate.metamodel.mapping.EmbeddableMappingType.compare(EmbeddableMappingType.java:267)
	at org.hibernate.metamodel.mapping.internal.EmbeddedForeignKeyDescriptor.compare(EmbeddedForeignKeyDescriptor.java:226)
	at org.hibernate.action.internal.CollectionAction.compareTo(CollectionAction.java:166)
	at org.hibernate.action.internal.CollectionAction.compareTo(CollectionAction.java:30)
	at org.hibernate.engine.spi.ExecutableList.add(ExecutableList.java:221)
	at org.hibernate.engine.spi.ActionQueue.addAction(ActionQueue.java:386)
	... omitted for brevity ...
```

Note that when the entity model is modified to remove the `FormInput` entity, the bug does _not_ occur. It may be related in some way to
the composite primary key on the `FormVersion` entity being used as a foreign key by the `FormInput` entity.

## Possibly Related Bug
We are aware of an existing bug relating to `deleteAll()`: https://github.com/spring-projects/spring-data-jpa/issues/1960. I believe this is likely separate to this 
issue however because the workaround suggested - add getters and setters to all fields - does not apply here as all fields on entities 
already have getters and setters.

## Test Case
The class `TestCase` performs the following two tests:
1. Creates two FormVersions and deletes them one at a time (succeeds).
2. Creates two FormVersions and deletes them together using `.deleteAll()` (fails).

## Running Locally
``mvn test``
