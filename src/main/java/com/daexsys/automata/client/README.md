Network Spec:

Packet structure:
byte: dictating length of following packet
byte: packet id
{ contents of packet }

#####################################################

Client to server:
Packet 0x00 (C - > S): Username
String

Packet 0x01 (C - > S): Alter tile
integer tile loc x
integer tile loc y
byte tile id

Packet 0x02 (C - > S): Chat message
String

Packet 0x03 (C - > S): Key status
boolean pressed or released
integer key

Packet 0x04 (C - > S): Get message of the day
integer number of players online
String motd

#####################################################

Server to client:
Packet 0x00 (S - > C): Server Name
String

Packet 0x01 (S - > C): Alter tile
integer tile loc x
integer tile loc y
byte tile id

Packet 0x02 (S -> C): Chat message
String

Packet 0x03 (S - > C): Single chunk load
16 x 16 x 2 wave of bytes

Packet 0x04 (S - > C): Player list update
boolean add or remove
String username