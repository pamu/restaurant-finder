CREATE TABLE restaurant_cuisines (
    id       UUID PRIMARY KEY,
    restaurant_id  UUID REFERENCES restaurants (restaurant_id),
    cuisine_id  UUID REFERENCES  cuisines (cuisine_id)
)
