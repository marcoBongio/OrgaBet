collection = "tennis"
pre = "db."+collection+".find()._addSpecial(\"$snapshot\",true).forEach(\n\tfunction(elem) {\n\t\tdb."+collection+".update(\n\t\t\t{_id: elem._id},\n\t\t\t{ $push: {\n"

post = "}});});"

bookmakersfootball = ["bet365", "betwin", "Blue Square", "Gamebookers", "Interwetten", "Ladbrokes", "Pinnacle", "Sporting Odds", "Sportingbet", "Stan James", "Stanleybet", "VC Bet", "William Hill"]
bookmakers = ["bet365", "betwin", "Centrebet", "Gamebookers", "Expekt", "Interwetten", "Ladbrokes", "Pinnacle", "Sportingbet", "Stan James", "William Hill", "Unibet"]
codes = ["B365", "BW", "CB", "GB", "EX", "IW", "LB", "PS", "SB", "SJ", "WH", "UB"]
typesfootball = ["H", "D", "A", "OVER", "UNDER"] #tipologie di scommesse
types = ["W", "L"]
typeLen = len(types)
len = len(bookmakers)

for j in range(0, typeLen):
	query = pre

	query += "\t\t\t\todds: { type:\"" + types[j] + "\", quotes: ["
	for i in range(0, len):
	    query = query + "{bookmaker: \"" + bookmakers[i] +"\", odd: elem." + codes[i]  + types[j] + "}"
	    if i < len-1: 
	    	query += ","

	query += " ] }\n"
	query = query + post
	print(query)
	print("")
