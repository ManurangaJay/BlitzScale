local stock_key = KEYS[1]
local quantity = tonumber(ARGV[1])

local current_stock = redis.call('GET', stock_key)

if not current_stock then
    return -1
end

current_stock = tonumber(current_stock)

if current_stock >= quantity then
    redis.call('DECRBY', stock_key, quantity)
    return 1
else
    return 0
end