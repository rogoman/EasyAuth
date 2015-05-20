[![Build Status](https://travis-ci.org/rogoman/EasyAuth.svg)](https://travis-ci.org/rogoman/EasyAuth)
[![GitHub license](https://img.shields.io/badge/license-Apache2-blue.svg)](https://github.com/rogoman/EasyAuth/blob/master/LICENSE)

# EasyAuth
======
EasyAuth is a lightweight library for Google Authenticator code generation and verification. You can use it both in server and client code.
It is fully compatible with the algorithm specified in [RFC 6238][RFC]. It supports a few other useful features, like keeping track
of the already used codes, or letting you specify the size of the window of accepted timestamp-related codes.

Installation
------------
To add this library to your code, you can either
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

[RFC]: https://tools.ietf.org/html/rfc6238