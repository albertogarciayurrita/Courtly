ALTER TABLE courts
DROP CONSTRAINT chk_courts_credits_cost_non_negative;

ALTER TABLE courts
ADD CONSTRAINT chk_courts_credits_cost_positive
CHECK (credits_cost > 0);