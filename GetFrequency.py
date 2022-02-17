import urllib 
import requests
import sys

encoded_query = urllib.parse.quote(sys.argv[1]) 
params = {'corpus': 'eng-us', 'query': encoded_query, 'topk': 1, 'format': 'tsv'} 
params = '&'.join('{}={}'.format(name, value) for name, value in params.items()) 
print(params)
response = requests.get('https://api.phrasefinder.io/search?' + params) 

sys.stdout.write(response.text.split("\t")[1])

