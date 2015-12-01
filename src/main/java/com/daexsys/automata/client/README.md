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

Packet 0x05 (C - > S): Update player location
integer x
integer y

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

Packet 0x03 (S - > C): Player list update
boolean add or remove
String username

Packet 0x04 (S - > C): Motd response
integer number of players online
String motd

Packet 0x05 (S -> C): Your entity id
integer id

Packet 0x06 (S - > C): Spawn entity
byte id
integer id
integer x
integer y

Packet 0x07 (S - > C): Despawn entity
byte id

Packet 0x08 (S - > C): Update entity location
integer id
integer x
integer y

Packet 0x09 (S - > C): Update player stat
byte stat_id
integer new_value

Packet 0x0A (S - > C): Update inventory slot
byte slot_id
byte item_id
byte amount

Packet 0x0A (S - > C): Single chunk load
16 x 16 x 2 wave of bytes

Packet 0x0B (S - > C): Mass chunk load
You can put it together.

Packet 0x0C (S - > C): Chunk despawn