-- wrk2 -R10000 -t12 -c400 -d5s -s deposit.lua http://localhost:8000/api/account/deposit
wrk.method = "POST"
wrk.body   = '{"accountId": 0, "amount": 1}'
wrk.headers["Content-Type"] = "application/json"
