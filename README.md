# Library Management System with a custom Blockchain Integration

This is a project made for the Data Structures course by a total of four team members: Mudasir Javed, Qamar Raza, Abdul Rehman Nasir and myself. 

[DEMO](https://www.youtube.com/watch?v=HDcN02dEskk)

## How to run?

Just run the code. Main is located in Main Class.

## How does the blockchain work? 

We have created an extremely simplified version of a blockchain. When a transaction is made, a Block object is created which is then added to the blockchain. The blockchain is an array of the blocks. One block contains details of the book being borrowed/returned and contains the hash of the blocks before it. This ensures that if a change is made in the previous blocks, the whole chain is disrupted. 
