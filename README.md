# jBlinky

jBlinky is an easy-to-configure Java library to set up CI build lights. It started as a Java port of the [blinky](https://github.com/perryn/blinky)
project.

The library is not only tied to CI job builds however, it also provides different types of `Probe` implementations and allows you to create your own!

## Getting started

The project is contained in three main modules: 
* `jblinky-core` provides the actual usb connection functionality.
* `jblinky-devices` is a collection of USB Light specifications from different vendors and manufacturers.
* `jblinky-probes` is a collection of various Probes that can be verified by jBlinky lights.

Needless to say, you are always able to create, provide and use your own devices and probes.

## How to use?

Just create a new `jBlinky` object and provide the `Probes` that you want to monitor.

```java
try {
  //Creates a new blinky and verifies a single job on a jenkins cruise control tray.
  jBlinky = new JBlinky();
  jBlinky.getLight().verifyProbes(new CCTrayProbe("https://yourdomain/jenkins/cc.xml", new String[] {"job-name"}));
} finally {
  jBlinky.close();
}
```

## Requirements and support

The library requires:

* Java 8

### Support

jBlinky currently supports this lights:

* [Delcom Gen2 Usb Light](https://www.delcomproducts.com/productdetails.asp?productnum=904008)
* [DreamCheeky WebMail Notifier](http://dreamcheeky.com/webmail-notifier)

It has only been tested on Mac OSX.
