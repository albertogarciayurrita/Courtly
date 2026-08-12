CREATE UNIQUE INDEX uq_active_reservation_slot
ON reservations (court_id, reservation_date, start_time)
WHERE status = 'CONFIRMED';