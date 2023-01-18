desc = "Basic subtraction (values loop around when sum is less than 0)"

function solve(hex1, hex2)
	num1 = tonumber(hex1, 16)
	num2 = tonumber(hex2, 16)
	total = num1-num2
	if total < 0 then
		total = total + tonumber("FFFFFF", 16) + 1
	end
	return string.format("%06x", total):sub(-6)
end