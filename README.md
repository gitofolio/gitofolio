# api_server_gitofolio
gitofolio API server

ResponseBody JSON 형태 예시
```JSON
{
    "name": "name",
    "profileUrl": "https://gitofolio.com",
	"userStat": {
        "totalVisitors": 3,
        "totalStars": 1
    },
	"userStatistics": {
        "visitorStatistics": [
            {
                "visitDate": "2021-11-08",
                "visitorCount": 111
            },
            {
                "visitDate": "2021-11-08",
                "visitorCount": 111
            }
        ],
        "refferingSites": [
            {
                "refferingSiteName": "https://gitofolio.com",
                "refferingDate": "2021-11-08"
            }
        ]
    },
	"portfolioCards": [
        {
            "portfolioCardArticle": "portfolio1",
            "portfolioCardStars": 1,
            "portfolioUrl": "https://gitofolio.com/portfolio/devxb"
        },
        {
            "portfolioCardArticle": "portfolio1",
            "portfolioCardStars": 1,
            "portfolioUrl": "https://gitofolio.com/portfolio/devxb"
        },
        {
            "portfolioCardArticle": "portfolio1",
            "portfolioCardStars": 1,
            "portfolioUrl": "https://gitofolio.com/portfolio/devxb"
        }
    ]
}
```