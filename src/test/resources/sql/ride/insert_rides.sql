INSERT INTO location(id, lat, lng, address)
    VALUES(1, '49.2259516', '28.4052628','Voiniv-Internatsionalistiv St, Vinnytsia, Vinnyts''ka oblast, Ukraine'),
    (2, '49.2251249', '28.4079674','Keletska Street, 87, Vinnytsia, Vinnyts''ka oblast, Ukraine'),
    (3, '49.2256954', '28.4404962','Keletska Street, 31, Vinnytsia, Vinnyts''ka oblast, Ukraine'),
    (4, '49.2327425', '28.4530055','Pyrohova St, 16а, Vinnytsia, Vinnyts''ka oblast, Ukraine'),
    (5, '49.2327426','28.4530056', 'Pyrohova St, 17а, Vinnytsia, Vinnyts''ka oblast, Ukraine');

INSERT INTO car(id, car_status, name, location_id, car_type)
    VALUES
    (1, 'FREE', 'Toyota49050', 1, 'ORDINARY'),
    (2, 'FREE', 'Nissan22073', 2, 'ORDINARY'),
    (3, 'FREE', 'Hyundai32785', 3, 'LUXURY'),
    (4, 'FREE', 'Kia33870', 4, 'PET'),
    (5, 'FREE', 'Tesla17093', 5, 'GROUP');

INSERT INTO ride(id, price, ride_date, ride_time, status, user_id, car_id, dest_location_id, start_location_id, rating)
    VALUES
    (1, 25.12, '2020-01-06', '09:58:21', 'RIDE_ENDED', 1, 1, 1,2,5),
    (2, 35.85, '2020-01-07', '09:58:21', 'RIDE_ENDED', 1, 2, 3,5,4),
    (3, 47.76, '2020-01-08', '09:58:21', 'RIDE_ENDED', 1, 3, 2,4,3),
    (4, 76.43, '2020-01-09', '09:58:21', 'RIDE_ENDED', 1, 4, 1,3,5);