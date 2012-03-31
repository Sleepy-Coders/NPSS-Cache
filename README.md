An remote cache server for [NPSS Framework](https://sourceforge.net/projects/npss/)

*Server is written in Java EE 6 Web*

MongoDB connection
------------------
In `/src/java/net/unikernel/npss/controller/resources`
rename "semple.connection.props" to "connection.props" аnd edit it to contain your mongodb connection data. More info on configuration details can be red in configuration file. 

MongoDB setup
-------------
More info on mongoDB: http://www.mongodb.org/
My own guide on how to setup on Solaris 11 express on [Sleepycoders Blog](http://sleepycoders.blogspot.com/2011/04/mongodb-solaris-11-express.html)

Web UI structure list
---------------------
Under `/resources/list/{interval}` in web interface you can see db structure with sizes. Interval can be omitted, but when set it's used for auto-refresh in seconds.