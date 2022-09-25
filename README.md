
# LolDataBatch
 
 -------------------

**[롤 전적 검색 프로젝트](https://github.com/kyo705/LolSearcher#lolsearcher)** 에 필요한 데이터 수집 및 데이터들의 통계를 위한 배치 프로그램

 프로젝트 생성 계기
-------------------
해당 프로젝트를 생성하게 된 계기는 **[롤 전적 검색 프로젝트](https://github.com/kyo705/LolSearcher#lolsearcher)** 에서 빠르고 정확한 통계 서비스를 제공하기 위해서 만들어졌다.   
롤 전적 검색 프로젝트에서 게임 캐릭터(챔피언)들의 승률, 아이템 승률, 카운터 챔피언 등을 제공하는 서비스가 필요로 했다. 
 그런데 해당 서비스를 만들기 시작할 때 문제가 발생했다. 그것은 바로, 통계 정보를 제공할 때 **속도 문제**였다. 통계 데이터 서비스를 요청할 때마다 집계함수를 이용한 쿼리문을 사용하기엔 집계해야할 데이터가 많아 처리 속도가 현저하게 느리다는 문제점이 있었다. 그래서 해당 문제를 해결하기 위해 통계 데이터 테이블을 따로 만들고 **매일마다** 서비스 이용자들이 적은 시간인 **새벽**에 전날 발생한 게임 데이터들을 **배치 처리**를 통해 통계 테이블에 데이터를 저장했다.   
 또한, 통계 서비스를 제공하기 위해서는 **많은 양**의 게임 매칭 데이터들이 필요했다. 그러나 게임 회사에서 데이터를 제공해주는 방식에서 **제한 상황(2분 동안 최대 100회 요청 가능)** 이 있기 때문에 **주기적**으로 게임 매칭 데이터들을 수집할 필요가 있었다. 그래서 **스케줄러**를 이용해 **2분마다** 게임 서버의 REST API 통신을 통해 데이터를 수집하는 로직을 만들었다.   

 프로젝트 기술 스택
-----------------
> - Java
> - Spring Boot
> - Spring Quartz
> - Spring Batch
> - DBMS 
>   - 실제 서버 환경 : MariaDB
>   - Test 환경 : h2

 프로젝트 구조
-----------------

![image](https://user-images.githubusercontent.com/89891704/175961577-7c159dae-4367-43be-8380-d8de1c671df0.png)   

![image](https://user-images.githubusercontent.com/89891704/175963624-a4a28556-2641-478f-94f5-8afb6aa50c2e.png)



주의 사항
-------------
JpaPagingReader를 사용할 때 패치 조인을 사용하면 안됌. 이유는 내부적으로 paging 처리를 하기 때문에 패치 조인 시 전부 메모리에 올라오고 그 상태에서 페이지 처리를 함. 따라서 메모리 낭비가 됌. 심한 경우 메모리 초과날 수 있음.    
또한 paging Reader를 사용할 때 정렬을 해야함. spring batch는 읽어올 데이터를 chunk 단위로 나눠 reader에서 반복해서 db로 부터 데이터를 조회한다. 반복하는 과정에서 page 단위만큼 잘라서 데이터베이스로부터 읽어오는데 이 때, 정렬 기준이 명확하지 않아 중복된 데이터를 읽어오거나 빠트린 데이터가 존재할 수도 있다. 따라서 paging reader를 사용할 때 반드시 정렬 기준을 명시해야 한다.   



메모장
------------
validator를 이용해 jobparameter의 유효성을 검증함 (defaultJobParametersValidator와 CustomValidator(ChampStaticJobParametersValidator)를 합친 CompositeJobParametersValidator를 사용함)

