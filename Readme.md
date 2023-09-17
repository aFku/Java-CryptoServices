# Java-CryptoServices

An application in form of microservices deployed in minikube. It allows you to:
- Generate password based on given specification
- Save your specification as profiles to be reused
- Generate password from your profile
- Save and fetch your encrypted password
- Save newly generated password

This application has been split into two REST API microservices. CryptoGenerator which is responsible for generating passwords and managing passwords. And CryptoPass that is responsible for saving and fetching passwords. The application also uses Kubernetes CustomResources, MySQL database and Keycloak to store information.

## Stack

- Java 17
- Spring boot
- Feign
- Java kubernetes client
- Ansible
- Minikube
- Keycloak
- MySQL
- Docker

## How to install

You need to have virtual machine or computer with CentOS 7 (CentOS Linux release 7.9.2009 (Core)) and one network interface with address within subnet 192.168.88.0/24.

To obtain Ansible please execute commands below:
```
sudo yum install python3
sudo pip3 install --upgrade --ignore-installed pip setuptools
pip3 install ansible
yum install sshpass
```

I used Ansible with version 2.11.12 and Python 3.6.8.

Next go to the `deployment/` directory and execute main playbook:

```
ansible-playbook -i inventory main.yaml --ask-become-pass --ask-pass
```

Provide your passwords and installation will set everything up.

These playbooks will:
- Install Docker
- Install and start Minikube
- Install MetalLB
- Create images required to build JARs
- Create images of microservices
- Deploy all required kubernetes infrastructure and our application

Before building microservices image all JUnit tests related to specific service will be run.

## How to use

After successful installation you can use
```
minikube kubectl -- get svc -n kong
```
And find external ip that belongs to kong ingress service. With this IP you can reach application from subnet 192.168.88.0/24. You can find list of endpoints below.

## API DOCS - Short version

### Requests payload

1. Keycloak JWT Payload:
```
{
    "username": String,
    "password": String,
    "client_id": String,
    "grant_type": String,
    "client_secret": String
}
```
Use this to obtain JWT token before sending requests to microservices

2. Password Profile Payload
```
{
    "profileName": String,
    "length": Int,
    "numbersAllowed": Bool,
    "uppercaseAllowed": Bool,
    "specialCharsAllowed": Bool,
    "excludedSpecialChars": String
}
```

3. Asymmetric Keys Profile Payload
```
{
    "profileName": String,
    "algorithm": String,
    "returnBase64": Bool
}
```

4. Password Store Payload
```
{
    "password": String,
    "name": String,
    "description": String,
    "key": String
}
```

5. Password Store From Generator Payload
```
{
    "passwordData": {
        "name": String,
        "description": String,
        "key": String
    },
    "properties": {
        "length": Int,
        "numbersAllowed": Bool,
        "uppercaseAllowed": Bool,
        "specialCharsAllowed": Bool,
        "excludedSpecialChars": String
    }
}
```

### Endpoints

All endpoints require header **Authentication** with `Bearer <JWT>`. You can obtain JWT from Keycloak by providing username and password

0. JWT Endpoint
```
[POST] /realms/cryptoservices/protocol/openid-connect/token
```
Body: Ad.1

1. Generate password with profile name:
```
[GET] /cryptogenerator/api/v1/generators/passwords
```
Params:

- **profileName** - Saved profile you want to use when generating password

2. Generate password with given specification:
```
[POST] /cryptogenerator/api/v1/generators/passwords
```
Body: Ad.2

3. Generate asymmetric keys with profile name:
```
[GET] /cryptogenerator/api/v1/generators/asymmetrics
```
Params:

- **profileName** - Saved profile you want to use when generating keys

4. Generate asymmetric keys with given specification:
```
[POST] /cryptogenerator/api/v1/generators/asymmetrics
```
Body: Ad.3

5. Get password profile
```
[GET] /cryptogenerator/api/v1/profiles/passwords
```
Params:

- **profileName** - Profile you want to fetch

6. Create new password profile
```
[POST] /cryptogenerator/api/v1/profiles/passwords
```
Body: Ad.2

7. Delete password profile
```
[DELETE] /cryptogenerator/api/v1/profiles/passwords
```
Params:

- **profileName** - Profile you want to delete

8. Get asymmetric keys profile
```
[GET] /cryptogenerator/api/v1/profiles/asymmetrics
```
Params:

- **profileName** - Profile you want to fetch

9. Create new asymmetric keys profile
```
[POST] /cryptogenerator/api/v1/profiles/asymmetrics
```
Body: Ad.3

10. Delete asymmetric keys profile
```
[DELETE] /cryptogenerator/api/v1/profiles/asymmetrics
```
Params:

- **profileName** - Profile you want to delete

11. Get all your stored passwords
```
[GET] /cryptopass/api/v1/passwords
```

12. Get specific password
```
[GET] /cryptopass/api/v1/passwords/{id: int}
```
Headers:

- **ENCRYPTION-KEY** - Key you passed when saving password. Will be used to decrypt your password.

13. Save password
```
[POST] /cryptopass/api/v1/passwords
```
Body: Ad.4

14. Delete stored password
```
[DELETE] /cryptopass/api/v1/passwords/{id: int}
```
Headers:

- **ENCRYPTION-KEY** - Key you passed when saving password. Will be used to decrypt your password.

15. Save password generated from given properties
```
[POST] /cryptopass/api/v1/passwords/properties
```
Body: Ad.5

16. Save password generated from given profile name
```
[POST] /cryptopass/api/v1/passwords/profiles
```
Params:

- **profileName** - Profile you want to use to generate password

Body: Ad.4