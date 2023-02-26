### DDD에서는 4개로 나뉜다

- 사용자 인터페이스: Controller
- 응용 계층(Application layer): Service
    - **서비스는 작업을 도메인이 하도록 조종하는 역할**
- 도메인 계층: Domain model
    - 도메인 = 비즈니스 모델
    - 실제 비즈니스 로직인 도메인에서 처리되어야 한다.
- 인프라 레빌: Repository

### Transaction Script

> 트랜잭션에 일어나는 일들이 나열되어 있는 코드들을 말한다. (비즈니스로직 & db layer 로직 짬뽕)

- 테스트하기 힘들다.
    - 비즈니스 로직을 테스트 하고 싶은데 그에 필요없는 repository 설정 등 부가적인 설정이 더 많이 들어가게 되어 비효율이다.

### 비즈니스 로직은 최대한 도메인이 구현해야한다!

비즈니스 로직이긴 하지만 구현로직을 짤만한 도메인이 없을경우는?

- 새로운 도메인을 만들어 그 도메인이 해당 비즈니스 역할을 갖게하자! (= 도메인 서비스)
    - ex) 가격계산 -> 상품, 유저, 쿠폰 모두 어색하니 새 도메인을 만들어 가격계산 로직을 작성
    - 로직 자체가 목적인 행위자 = 도메인 서비스 (서비스는 아님)

도메인 서비스로 만들었을 때 새로운 도메인이 될 수 있는지 생각해보자!

### 추상화

> 책임을 정의하는 과정
>
> 복잡한 자료, 시스템, 모듈등으로 부터 핵심적인 개념 또는 기능을 간추려 내는 것

### 어디까지 추상화 해야하는가

1. 시스템 외부 연동은 추상화 하자

- JpaRepository 와의 강결합을 끊어내자 (DIP)
    - 결합도가 낮아짐: 디비가 바뀔 경우 비즈니스 코드에 영향이 없다. (OCP)
        - ex) 테스트 할 때 테스트용 repository로 갈아끼워서 사용이 가능하다. (h2, mockito 필요없이 테스트 가능)
- Client 로 외부와 통신할 때도 강결합을 끊어내자

```kotlin
// 응용계층
class Service(
    private val repository: Repository
)

// 응용계층
interface Repository {}

// 인프라 계층
class RepositoryImpl(
    private val entityJpaRepository: EntityJpaRepository,
) : Repository {
    //... find, save,,,,
}

// 인프라계층
interface EntityJpaRepository : JpaRepository<Entity, Long>
```

이렇게 구현하는 경우도 많다.

```kotlin

import org.springframework.stereotype.Repository

interface EntityRepository : JpaRepository<Entity, Long>

@Repository
class EntityRepositoryImpl : EntityRepository
```

2. 도메인 레이어를 추가하자

- 도메인 객체는 도메인들간의 협력만 필요하기 때문에 인프라 레이어에 있는 클래스를 사용하지 않는다. (하지만 이건 갑론을박 중)
    - 즉 entity를 조회하거나 save를 하는 인프라 로직이 없어야 한다.
- 협력할 도메인들은 service에서 전달해주는 방식이다.


