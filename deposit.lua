-- wrk2 -R10000 -t36 -c400 -d5s -s deposit.lua http://localhost:8000/api/account-async/deposit
-- wrk -t36 -c400 -d5s -s deposit.lua http://localhost:8000/api/account-async/deposit

wrk.method = "POST"
wrk.body   = '{"accountId": 2, "amount": 1}'
wrk.headers["Content-Type"] = "application/json"
