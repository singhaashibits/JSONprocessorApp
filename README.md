Spring Boot JSON Processing API
This Spring Boot application has two JSON files:

location.json – Contains location id, latitude, and longitude.
metadata.json – Contains additional data (type, rating, reviews).
The application merges these datasets and provides APIs to:

Count locations by type (e.g., restaurants, hotels, cafes).
Calculate the average rating per type.
Identify the location with the highest number of reviews.
Detect locations with incomplete data (missing fields).
