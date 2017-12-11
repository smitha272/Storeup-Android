from sklearn.cluster import KMeans
import numpy as np
import matplotlib.pyplot as plt
import mysql.connector
from mysql.connector import errorcode
import urllib.request
import requests
import json
from bottle import run, get, request

@get('/getRecommendations')
def getRecommendations():
    store = request.params.get('store')
    city = request.params.get('city')
    x1 = []
    x2 = []
    rowCount = 0
    try:
        cnx = mysql.connector.connect(user='krishnark1993',
                                      password='krishna1993',
                                      host='cmpe275.cgy1wsewzyvd.us-west-1.rds.amazonaws.com',
                                      database='storeup')
        connection = cnx.cursor()
        query = ("SELECT user_details.latitude as lat, user_details.longitude as lng from storeup.user_details, storeup.receipt_details " 
                "where receipt_details.user_name = user_details.email and receipt_details.store_name = %s and user_details.city = %s")
        connection.execute(query,(store,city))
        if connection.rowcount == 0:
            print("No results found")
            return {}
        else:
            for (lat, lng) in connection:
                rowCount = rowCount + 1
                x1.append(lat)
                x2.append(lng)
            x1 = np.array(x1)
            connection.close()

    except mysql.connector.Error as err:
        if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
            print("Something is wrong with your user name or password")
        elif err.errno == errorcode.ER_BAD_DB_ERROR:
            print("Database does not exist")
        else:
            print("Something Wrong with the Query!")
            print(err)
    else:
        cnx.close()

    print('Total number of Customers')
    print(rowCount)

    # create new plot and data
    if len(x1) == 0 or len(x2) == 0:
        return {}
    plt.plot()
    X = np.array(list(zip(x1, x2))).reshape(len(x1), 2)
    #colors = ['b', 'g', 'c', 'r']
    #markers = ['o', 'v', 's', 's']

    # KMeans algorithm
    K = 6
    kmeans_model = KMeans(n_clusters=K).fit(X)

    print("All the Suggested Locations: ")
    print(kmeans_model.cluster_centers_)
    centers = np.array(kmeans_model.cluster_centers_)
    r = {}
    recommendations = {}
    validRecomendationCount = 0
    for center in centers:
        radiusCheck = "https://maps.googleapis.com/maps/api/place/radarsearch/json?location=" \
                      + str(center[0]) + "," + str(center[1]) + \
                      "&radius=500&type=grocery_or_supermarket&keyword=" + store + "&key=AIzaSyBUt6J8i7uZm7ofoVxH1yoXkFhvgdzMJwg"
        r.update(json.loads(requests.get(radiusCheck).content))
        for key, value in r.items():
            if key == 'status':
                if value == 'ZERO_RESULTS':
                    validRecomendationCount = validRecomendationCount + 1
                    recommendations[center[0]] = center[1]
    '''plt.plot()
    plt.title('k means centroids')

    for i, l in enumerate(kmeans_model.labels_):
        plt.plot(x1[i], x2[i], color=colors[l], marker=markers[l], ls='None')
        plt.ylim([-121.92, -121.91])
        plt.xlim([37.33, 37.34])

    plt.scatter(centers[:, 0], centers[:, 1], marker="x", color='r')
    plt.show()'''
    print("Total Valid Recomendations: ")
    print(validRecomendationCount)
    print("Valid Recommended Locations: ")
    print(recommendations)
    return recommendations


run(reloader=True, debug=True)
