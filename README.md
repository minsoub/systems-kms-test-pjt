# Spring boot에서 aws kms를 이용한 프로퍼티 값 암호화 하기 
2022.03.06 14:00 write by jms   
![ex_screenshot](./images/desc01.png)
db 정보나 apk key 등 중요 정보에 대해서 git 계정에 들어가면 문제가 발생할 수 있다. 이런 보안이 필요한 값들은 처음부터 암호화해서 등록할 수 있다. AWS KMS를 이용해서 sprint boot 프로젝트의 프로퍼티 값을 암호화 할 수 있다.   
```yaml
datasource:
  url: jdbc:mysql://localhost:33306/test?serverTimezone=UTC&characterEncoding=UTF-8
  username: xxxxx
  password: xxxxxxxx
  driver-class-name: com.mysql.cj.jdbc.Driver
```
### AWS IAM 생성
kms는 AWS IAM USER나 ROLE을 통해서 권한을 가질 수 있다.  따라서 local에서 사용할 iam이나 role을 만들어서 수행합니다.   
해당 사용자에게 AmazonSSMFullAccess, AmazonSSMReadOnlyAccess 정책을 추가해야 한다.

### KMS(Key Management Service) 생성
- Key Alias : systems-dev-key
- Key policy
  - Key administrators : xxxxx
  - Key users : Systems-ParameterStoreRule (Role)
### AWS System Manager의 StoreParameter 추가
- Name : /systems/databaseinfo_dev/jdbcurl
- Value : xxxxx (SecureString)
### Systems-ParameterStoreRule
```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "ssm:GetParametersByPath",
                "ssm:GetParameter"
            ],
            "Resource": "arn:aws:ssm:ap-northeast-2:xxxxxxxxx:parameter/*"
        },
        {
            "Effect": "Allow",
            "Action": [
                "kms:Decrypt",
                "kms:Encrypt",
                "kms:GenerateDataKey"
            ],
            "Resource": [
                "arn:aws:kms:ap-northeast-2:xxxxxxxxx:key/xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
            ]
        }
    ]
}
```
### bootstrap.yml
```yaml
# {prefix}/{name}{profileSeparator}{profile}/parameter.key
aws:
  paramstore:
    enabled: true
    prefix: /systems
    name: databaseinfo
    profileSeparator: _
  s3:
    enabled: true
```
### application.yml
```yaml
spring:
  profiles:
    active: dev

cloud.aws:
  region.static: ap-northeast-2
  stack.auto: true

---
srping:
  profiles: dev
  application:
    name: Kms-test-pjt

cloud:
  aws:
    credentials:
      profile-name: dev
    stack.auto: false

```
### aws configure --profile dev 수행
위에서 aws credentials의 profile-name을 dev로 정의했고 Parameter store에서도 dev로 데이터를 찾는다.   
```shell
$> aws configure --profile dev
```
### Usage
```java
package com.bithumbsystems.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@NoArgsConstructor
@Configuration
public class ParameterStoreProperties {
    @Value("${jdbcurl}")
    private String dbUrl;
}
```
### Test Code
```java
package com.bithumbsystems.config;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ParameterStorePropertiesTest {
    @Autowired
    private ParameterStoreProperties properties;

    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }
    @Test
    void local_parameter_test() throws Exception {
        assertThat(properties.getDbUrl()).isEqualTo("test");
    }
}

```
## Window AWS CLI2 설치 
윈도우 command 창에서 아래와 같이 수행   
```shell
msiexec.exe /i https://awscli.amazonaws.com/AWSCLIV2.msi
```
## Throubleshooting
```shell
com.amazonaws.AmazonClientException: EC2 Instance Metadata Service is disabled
```
EC2를 사용하지 않는데도 위와 같이 에러가 발생한다. 아래 정보를 VM Arguments에 추가한다.   
Modify Run Configuration을 통해서 수정할 수 잇다.    
-Dcom.amazonaws.sdk.disableEc2Metadata=true   
실행하면 com.amazonaws.AmazonClientException: EC2 Instance Metadata Service is disabled 에러가 나올 것이다. 이것은 우리가 의도한 것이다.