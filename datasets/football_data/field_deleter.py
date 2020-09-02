collection = "tennis"
pre = "db."+collection+".update({}, {$unset: {"

post = "}, {multi:true} );"

bookmakersfootball = ["bet365", "betwin", "Blue Square", "Gamebookers", "Interwetten", "Ladbrokes", "Pinnacle", "Sporting Odds", "Sportingbet", "Stan James", "Stanleybet", "VC Bet", "William Hill"]
bookmakers = ["bet365", "betwin", "Centrebet", "Gamebookers", "Expekt", "Interwetten", "Ladbrokes", "Pinnacle", "Sportingbet", "Stan James", "William Hill", "Unibet"]

codesfootball = ["B365", "BW", "BS", "GB", "IW", "LB", "PS", "SO", "SB", "SJ", "SY", "VC", "WH"]
codes = ["B365", "BW", "CB", "GB", "EX", "IW", "LB", "PS", "SB", "SJ", "WH", "UB"]

typesfootball = ["H", "D", "A", "OVER", "UNDER"] #tipologie di scommesse
types = ["W", "L"]

typeLen = len(types)
len = len(bookmakers)

for j in range(0, typeLen):
	query = pre
	
	for i in range(0, len):
	    query = query + codes[i] + types[j] + ":1"
	    if i < len-1: 
	    	query += ", "
	query += "}"
	query = query + post
	print(query)
	print("")
