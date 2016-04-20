# ignite example

this code creates ignite indexed collection with 100k records having two different types of index 1) on single column 2) on group of two columns, and then benchmark search for 1 sec.
this also show iginte's capability of cross cache querying with standard sql using group by abd aggregate functions.
It creates two caches Trade {sedol, side, amount} and SecurityDetail {sedol, sector}. with help of sql, we are able to get sum of buy/sell trades amount sector wise.
In order to do this, these caches were joined on sedol and grouped by sector and side.


Output
[2016-04-20 13:36:02][INFO ][main][App] - start populating non indexed collection
[2016-04-20 13:36:07][INFO ][main][App] - collection populated with: 100000 recs in: 4139 ms
[2016-04-20 13:36:07][INFO ][main][App] - start populating indexed collection
[2016-04-20 13:36:17][INFO ][main][App] - collection populated with: 100000 recs in: 10709 ms
[2016-04-20 13:36:17][INFO ][main][App] - start populating Security detail cache
[2016-04-20 13:36:21][INFO ][main][App] - collection populated with: 100000 recs in: 4206 ms
[2016-04-20 13:36:21][INFO ][main][App] - benchmarking equals for non-indexed
[2016-04-20 13:36:23][INFO ][main][App] - performed: 10 searches in: 1 secs
[2016-04-20 13:36:23][INFO ][main][App] - benchmarking equals for indexed
[2016-04-20 13:36:24][INFO ][main][App] - performed: 8661 searches in: 1 secs
[2016-04-20 13:36:24][INFO ][main][App] - benchmarking equals for non-indexed on two fields
[2016-04-20 13:36:25][INFO ][main][App] - performed: 7 searches in: 1 secs
[2016-04-20 13:36:25][INFO ][main][App] - benchmarking equals for indexed on two fields (both indexed)
[2016-04-20 13:36:26][INFO ][main][App] - performed: 26972 searches in: 1 secs
[2016-04-20 13:36:26][INFO ][main][App] - cross cache join - finding out sum of all buy and sell trades for all sectors
[2016-04-20 13:36:31][INFO ][main][App] - [AUTOMOBILES, BUY, 5148343.881680102]
[2016-04-20 13:36:31][INFO ][main][App] - [AUTOMOBILES, SELL, 5003207.724372575]
[2016-04-20 13:36:31][INFO ][main][App] - [BANKING_AND_FINANCIAL_SERVICES, BUY, 5051623.285913207]
[2016-04-20 13:36:31][INFO ][main][App] - [BANKING_AND_FINANCIAL_SERVICES, SELL, 5013676.028884658]
[2016-04-20 13:36:31][INFO ][main][App] - [IT_SERVICES, BUY, 4952057.756273833]
[2016-04-20 13:36:31][INFO ][main][App] - [IT_SERVICES, SELL, 4935281.902042132]
[2016-04-20 13:36:31][INFO ][main][App] - [GAS_AND_COAL, BUY, 5037114.187880588]
[2016-04-20 13:36:31][INFO ][main][App] - [GAS_AND_COAL, SELL, 4945543.601136833]
[2016-04-20 13:36:31][INFO ][main][App] - [PHARMACEUTICALS_AND_HEALTCARE, BUY, 4988796.718544621]
[2016-04-20 13:36:31][INFO ][main][App] - [PHARMACEUTICALS_AND_HEALTCARE, SELL, 4939353.123705272]
[2016-04-20 13:36:31][INFO ][main][App] - total time taken to run cross cache with 2 level of grouping: 4224 ms
