# Chat-Application

A chat application that can be used to talk to friends. Any number of clients can connect to a given host, allowing possibilities of either one-on-one or group chats.

## Encryption

The chat application utilizes cryptography for increased security. In particular, it uses an affine cipher to encrypt sent messages and decrypt received messages. Each cryptography device is unique per session, so a brute forced attack is ineffective.

## Usage
Here's how to run the chat application.
### Server
To run the server, run the following command on the terminal:
```
java Server <port_number>
```
### Client
To run a client, run the following command on the terminal:
```
java Client <host> <port_number>
```
### Testing
To test the connection, run the following command on the terminal:
```
java ChatApplication <num_clients>
```
