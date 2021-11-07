insert into users(username ,password ,enabled)values('admin','admin',true)
insert into users(username ,password ,enabled)values('api','api',true)
insert into users(username ,password ,enabled)values('user','user',true)

insert into authorities(username ,authority) values ('admin','ROLE_ADMIN')
insert into authorities(username ,authority) values ('api','ROLE_API')
insert into authorities(username ,authority) values ('user','ROLE_USER')