# jBlinky
[![Build Status](https://travis-ci.org/rastadrian/jblinky.svg?branch=develop)](https://travis-ci.org/rastadrian/jblinky) [![Coverage Status](https://coveralls.io/repos/github/rastadrian/jblinky/badge.svg?branch=develop)](https://coveralls.io/github/rastadrian/jblinky?branch=develop) [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) 

jBlinky is an easy-to-configure Java library to set up CI build lights. It started as a Java port of the [blinky](https://github.com/perryn/blinky) project.

The library is not only tied to CI job builds however, it also provides different types of `Probe` implementations and allows you to create your own!

For use cases, documentation and more details, please visit the [jBlinky Wiki](https://github.com/rastadrian/jblinky/wiki) site.

## Getting started

The project is contained in three main modules: 
* `jblinky-core` provides the actual USB Lights functionality, including the Probe Verification process.
* `jblinky-devices` is a collection of USB Light specifications from different vendors and manufacturers.
* `jblinky-probes` is a collection of various Probes that can be verified by jBlinky lights.

Needless to say, you are always able to create, provide and use your own [custom devices](https://github.com/rastadrian/jblinky/wiki/USB-Devices#create-your-own-usb-light-specification) and [custom probes](https://github.com/rastadrian/jblinky/wiki/Probes#custom-probes).

## How to use?

Just create a new `jBlinky` object, access one of the lights and use them! 

```java
//Creates a new blinky, retrieves the first connected light and blinks between red and green.
new JBlinky().getLight().test();
```

You can also provide the `Probes` that you want to monitor.

```java
//Creates a new blinky and verifies a single job on a Jenkins Cruise Control Tray.
Probe probe = new CCTrayProbe("https://yourdomain/jenkins/cc.xml", new String[] {"job-name"});
Light light = new JBlinky().getLight();
light.verifyProbes(probe);
```

Please visit the [jBlinky & Lights Wiki](https://github.com/rastadrian/jblinky/wiki/jBlinky-&-Lights) page for more detailed use cases!

## Requirements and support

The library requires:

* Java 8 Runtime Environment

### Support

jBlinky currently supports this lights:

* [Delcom Gen2 Usb Light](https://www.delcomproducts.com/productdetails.asp?productnum=904008)
* [DreamCheeky WebMail Notifier](http://dreamcheeky.com/webmail-notifier)

It support and has been tested on:

* Mac OSX
* MS Windows (tested on 8.1).

## License
This project is licensed under the OSI [MIT License](https://opensource.org/licenses/MIT).
