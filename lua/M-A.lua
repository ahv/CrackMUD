desc = "Basic addition (values loop around to fit 6 hex digits)"

function solve(hex1, hex2)
	num1 = tonumber(hex1, 16)
	num2 = tonumber(hex2, 16)
	total = num1+num2
	return string.format("%06x", total):sub(-6)
end