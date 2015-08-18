
The format is as follows:- 
<spam label,sentiment label> <tab> <text> 
<tab> means you have to actually press a tab after filling in the details of the class labels 

Spam Label has the following values :- 
 
0 = Spam
1 = Ham/Not Spam 
 
Sentiment Label Has the following values:- 
 
-1 = Negative
0 = Neutral
1 = Positive

If the spam label is 0 (i.e it's a spam then make the sentiment label as null refer to the example below)
<0,null>	i miss you. fifa 

<1,1>	RT @ZiaDomic: Empowerment Through Fashion @NoondayStyle #fblogger #ootd #style @dexterbrown1 @FemaleBloggerRT @7FAM @ShoeDazzle 
<1,0>	RT @heyitstone: Summer Friday! Pack a set of workout clothes for lounging and a quick beach #workout #fitness #Summer shot for @7FAM 
<1,-1>	@7FAM Can't DM you without a follow. Hoping to get this situation settled sooner than 10 weeks! 
<1,0>	Summer Friday! Pack a set of workout clothes for lounging and a quick beach #workout #fitness #Summer shot for @7FAM 