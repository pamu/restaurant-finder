CREATE TABLE restaurant_cuisines (
    id       SERIAL PRIMARY KEY,
    restaurant_id  UUID REFERENCES restaurants (id),
    cuisine_id  UUID REFERENCES  cuisines (id)
)