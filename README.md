JMiNya
=========
https://github.com/nyatla/JMiNya/

Copyright (C)2014 nyatla.jp




 What is this?
----------------------------------------------------------------------
A pure-Java sCrypt coin miner for tcp+strutum mining pool.
GUI application and hashing library.

It can  mining on any stratum+tcp server.
JMiNya use JMiner's hashing implementation and part of source code.


It based on JMiner litecoin miner by pooler/JMiner.

https://bitcointalk.org/index.php?topic=52386.0


日本語のマニュアルはこちらをご覧ください。

https://github.com/nyatla/JMiNya/release/readme.ja.txt



Feature
----------------------------------------------------------------------

 - Have a GUI application.
It has a simple GUI and easy setting.
Some preset pools exist.(MONA/NYAN/LTC and BEER!)

 - Full Pure java implementation.
It can run any machine.

 - Stratum+tcp server support.
It can mining on any stratum+tcp:// server!
  
 - Vely slow hash late.
This is pure java CPU miner that is very slow hasing.
1kH/s -2kH/s per core.
(I recommend cgminer or cudaminer if you have GPU.)

How to Use
----------------------------------------------------------------------

1. Start jminya.jar in release directory. for windows, click .bat file.
2. After window opened, set mining parameters.
    - Stratum Server address
     - MiningPool - Select manual or preseted pools.
     - ServerURL  - Enter stratum+tcp server addrss when manualy.
    - Worker Setting
     - Name     - Name of worker
     - Password - Password of worker
    - Miner setting
     - Number of Setting - Number of thread for mining.
3. Press "Start Mining". mining is started.


LICENCE
----------------------------------------------------------------------
  See https://github.com/nyatla/JMiNya/LICENCE


DONATION
----------------------------------------------------------------------
If you like it, please donate coins.

    MONA:M8thudPD5TqwQrhrfksrNZTCRxwSBS9HGV
    NYAN:KAqg41G2HnUEH5mEJdfEZmoYWLGqyhLPvh
    BEER:56fqErAcHTvMHwLBRHiKKJAmAJB2GGJ7c3
    LTC :LaSWCtBGTxWdxNJWS17AUw9eYRS9JHnFin


Special thanks
----------------------------------------------------------------------

  Satoshi Nakamoto-san
    Thank you for bitcoin dream.

  pooler-san
    Thank you for reference implementation of Java scrypt hasher.
    https://github.com/pooler/JMiner

  Slush's pool
    Thank you for stratum-protocol reference.
    http://mining.bitcoin.cz/stratum-mining
