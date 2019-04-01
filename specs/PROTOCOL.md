
# Protocol

## Specifications

- What transport protocol do we use?

    TCP

- How does the client find the server (addresses and ports)?

    At the moment the client needs to write the server (host ip) in the docker files before runnning the install-run-sh file. (local network)<br/>
    The server is listening on port 2019.

- Who speaks first?

    When the client connects to the server, the server start by greeting the client and asking him to enter a command.

- What is the sequence of messages exchanged by the client and the server?
  - server : greeting message
  - client : some command (that is not an exit command)
  - server : answer (response to a computation, error indication or help display)
  - ...
  - ...
  - client : enters an exit command ("exit" or "quit")
  - server : sends "bye !" and closes connection
  - client : receives the "bye !" line and closes

- What happens when a message is received from the other party?
  - from client to server : server parses the message and answers accordingly
  - from server to client : client reads server stream until an "msg_end" line that tells him that he can send a new command

- What is the syntax of the messages? How we generate and parse them?
  - list of commands accepted by the server (user inputs)
    - quit : exit the program and close connection
    - exit : same as quit
    - help : displays list of commands
    - add a b : computes a + b and return the answer
    - sub a b : computes a - b and return the answer
    - mul a b : computes a * b and return the answer
    - div a b : computes a / b and return the answer (division by 0 returns +/- infinity)
  - list of commands received by the client (excepting results and errors) (server automatic messages)
    - bye ! : stop the program and close connection
    - msg_end : tell the client that the server has send the entire message and is ready for another instruction

- Who closes the connection and when?

    The server closes the connection when he receives the instruction to do so. (quit or exit)<br/>
    When closing, he tells the client to end the program and close the connection (bye !)