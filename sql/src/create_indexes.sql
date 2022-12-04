DROP SEQUENCE IF EXISTS order_num;
CREATE SEQUENCE order_num START WITH 501;

DROP SEQUENCE IF EXISTS update_num;
CREATE SEQUENCE update_num START WITH 51;

DROP SEQUENCE IF EXISTS user_num;
CREATE SEQUENCE user_num START WITH 101;

CREATE OR REPLACE LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION set_order_number()
RETURNS "trigger" AS
$BODY$
BEGIN
        NEW.orderNumber = nextval('order_num');
        return NEW;
END;
$BODY$
LANGUAGE plpgsql VOLATILE;

CREATE OR REPLACE FUNCTION set_update_number()
RETURNS "trigger" AS
$BODY$
BEGIN
        NEW.updateNumber = nextval('order_num');
        return NEW;
END;
$BODY$
LANGUAGE plpgsql VOLATILE;

CREATE OR REPLACE FUNCTION set_user_number()
RETURNS "trigger" AS
$BODY$
BEGIN
        NEW.userID = nextval('order_num');
        return NEW;
END;
$BODY$
LANGUAGE plpgsql VOLATILE;


DROP TRIGGER IF EXISTS set_order_number_trigger ON Orders;
DROP TRIGGER IF exists set_update_number_trigger ON ProductUpdates;
DROP TRIGGER IF exists set_user_number_trigger ON Users;

CREATE TRIGGER set_order_number_trigger
BEFORE INSERT
ON Orders
FOR EACH ROW
EXECUTE PROCEDURE set_order_number();

CREATE TRIGGER set_update_number_trigger
BEFORE INSERT
ON ProductUpdates
FOR EACH ROW
EXECUTE PROCEDURE set_update_number();

CREATE TRIGGER set_user_number_trigger
BEFORE INSERT
ON Users
FOR EACH ROW
EXECUTE PROCEDURE set_user_number();


