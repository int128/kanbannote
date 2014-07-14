Kanban Note service
===================

App Engine application at https://kanbannote.appspot.com


How to build
------------

Unpack the App Engine SDK and set the environment variable `APPENGINE_SDK_HOME`.

```bash
# ~/.bashrc
export APPENGINE_SDK_HOME="$HOME/App/appengine-java-sdk-x.y.z"
```

Then, build and start a development server.

```
$ gulp
$ sbt

> appengineDevServer
```


Architecture
------------

This project contains following:

  * Scala 2.10
  * Unfiltered filter
  * Scalate
    * Scalate cache support
    * Scalate generating support with xsbt-scalate-generator
  * App Engine support with sbt-appengine
  * Assets management with gulp and bower
