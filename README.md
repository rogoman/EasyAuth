[![Build Status](https://travis-ci.org/rogoman/EasyAuth.svg)](https://travis-ci.org/rogoman/EasyAuth)
[![GitHub license](https://img.shields.io/badge/license-Apache2-blue.svg)](https://github.com/rogoman/EasyAuth/blob/master/LICENSE)

# EasyAuth
EasyAuth is a lightweight library for Google Authenticator code generation and verification. You can use it both in server and client code.
It is fully compatible with the algorithm specified in [RFC 6238][RFC]. It supports a few other useful features, like keeping track
of the already used codes, or letting you specify the size of the window of accepted timestamp-related codes.

Installation
------------
This library requires at least Java 7. To add this library to your code, you can either

* clone this repository and compile the library by using maven from the repository root folder
        
        mvn clean package
         
* reference the library in your build environment
    
    * Maven
            
            <dependency>
                <groupId>com.rogoman</groupId>
                <artifactId>easyauth</artifactId>
                <version>0.1.0</version>
            </dependency>
            
    * Gradle
        
            compile 'com.rogoman:easyauth:0.1.0'

Usage
-----

Basic usage assumes you want to use a code interval length compatible with Google Authenticator (30 seconds) 
and are good with using the same code multiple times (if done within the same 30 second interval).

* generate a new secret key

        String secret = TimeAuthenticator.generateKey();

* get current valid code for a given secret key

        Authenticator auth = new TimeAuthenticator();
        auth.getCode(secret);
        
* validate the received code (`userIdentifier` is a string uniquely identifying a user that used the code for authentication)

        Authenticator auth = new TimeAuthenticator();
        boolean isCodeValid = auth.checkCode(secret, code, userIdentifier);

If you want to enforce using the same code for a single user only once within the same interval, you can use the `InMemoryUsedCodesManager`
or implement your own used-codes manager (by implementing the `UsedCodesManager` interface).

        UsedCodesManager<String> codeManager = new InMemoryUsedCodesManager();
        Authenticator auth = new TimeAuthenticator(codeManager);
        
You can also provide your own code interval length or specify the code manager cleaning period. For the full list of available
constructors and methods please reference the Javadoc pages or see the source code of each class.

Javadoc
-------
You can compile javadoc pages by calling
    
    mvn clean package -P javadoc
    
The javadoc HTML pages will be produced in the `target/apidocs` subfolder.

Contributing
------------
I love pull requests! If you feel that you can add value to the library by implementing new features or fixing bugs, please do so! 

[RFC]: https://tools.ietf.org/html/rfc6238