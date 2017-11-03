======================================================================
JMinya
 version 1.0.2
======================================================================

Copyright (C)2014 nyatla.jp

https://github.com/nyatla/JMiNya
wm(at)nyatla.jp



----------------------------------------------------------------------
 What is this?
----------------------------------------------------------------------
  This is pure-java sCrypt mining software.
  It can  mining on any stratum+tcp server.
  JMiNya use JMiner's hashing implementation and part of source code.

----------------------------------------------------------------------
 Feature
----------------------------------------------------------------------

  - Have a GUI.
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

----------------------------------------------------------------------
 How to Use
----------------------------------------------------------------------

  1. Start jminya.jar. for windows, click .bat file.
  2. After window opened, set mining parameters.
    Stratum Server address
    +-MiningPool - Select manual or preseted pools.
    +-ServerURL  - Enter stratum+tcp server addrss when manualy.
    
    Worker Setting
    +-Name     - Name of worker
    +-Password - Password of worker

    Miner setting
    +-Number of Setting - Number of thread for mining.
  3. Press "Start Mining". mining is started.

----------------------------------------------------------------------
 LICENCE
----------------------------------------------------------------------
  See LICENCE.

----------------------------------------------------------------------
 DONATION
----------------------------------------------------------------------
  If you like it, please donate coins.

  MONA:M8thudPD5TqwQrhrfksrNZTCRxwSBS9HGV
  NYAN:KAqg41G2HnUEH5mEJdfEZmoYWLGqyhLPvh
  BEER:56fqErAcHTvMHwLBRHiKKJAmAJB2GGJ7c3
  LTC :LaSWCtBGTxWdxNJWS17AUw9eYRS9JHnFin


----------------------------------------------------------------------
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
