# IpEventService

## Background

In this coding question, we're looking to differentiate between the normal set of IPs that a client communicates with and abnormal ones that might indicate reaching out to a C&C server for instructions. The clients will be sending events to the server, each containing a [Protocol Buffer](https://code.google.com/p/protobuf/)-encoded description of the app in question and the IP address it connected to. See [ip_event.proto](lookout/src/main/java/com/lookout/proto/ip_event.proto) for the protobuf definition. The server's job (and yours to implement) is to collect these events and respond to queries about them.

The [sample client] will simulate the stream of events from a variety of clients. The server should handle POSTs to '/events' with a Content-Type of 'application/octet-stream'

Once the client is done sending simulated events, it will make a call to the RESTful API to check that the server captured and analyzed the event stream correctly. The server should handle a GET to '/events/:app_sha256'. When called with a JSON accept header, it should return a description of all events for that app_sha256. This should be of the form:

    {
      'count':NUMBER_OF_EVENTS,
      'good_ips':LIST_OF_GOOD_IPS,
      'bad_ips':LIST_OF_BAD_IPS
    }

For a given app, this exercise assumes it will only hit hosts within a /28 CIDR netblock (and thus have 14 valid IPs that it might hit, accounting for gateway and network addresses). The server should determine which IPs make up that /28 and return them as good_ips, and any other IPs are returned as bad_ips.  The netblock containing the good_ips will be the most dense netblock for a particular app-sha -- the netblock with the most recorded ip events.

The server must also handle a DELETE to '/events'. This is used to reset counters and data in between test runs.






# Service can be tested as :

./bin/lookout_backend_coding_q2_client --host localhost  --tcp 8080

./bin/lookout_backend_coding_q2_client --host 54.86.73.176  --tcp 8080
