SET GLOBAL validate_password.policy=LOW;
SET GLOBAL validate_password.length=4;
SET GLOBAL validate_password.mixed_case_count=0;
SET GLOBAL validate_password.special_char_count=0;
SET GLOBAL validate_password.number_count=0;
SET GLOBAL validate_password.check_user_name=0;
CREATE USER 'fred'@'%' IDENTIFIED BY 'fred';