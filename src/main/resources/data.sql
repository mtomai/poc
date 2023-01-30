insert into USERS values (1,'Mario', 'Rossi', 43, 'M', '20-11-1979');
insert into USERS values (2,'Maria', 'Verdi', 21, 'F', '20-11-2001');
insert into USERS values (3,'Francesco', 'Blu', 34, 'M', '20-11-1988');
insert into USERS values (4,'Sara', 'Gialli', 51, 'F', '20-11-1971');
insert into USERS values (5,'Giovanni', 'Viola', 23, 'M', '20-11-1999');
insert into USERS values (6,'Eleonora', 'Celeste', 28, 'F', '20-11-1994');
insert into USERS values (7,'Marco', 'Grigi', 64, 'M', '20-11-1958');
insert into USERS values (8,'Cristiana', 'Azzurri', 15, 'F', '20-11-2007');
insert into USERS values (9,'Giorgio', 'Neri', 13, 'M', '20-11-2009');
insert into USERS values (10, 'Isabella', 'Bianchi', 18, 'F', '20-11-2004');

insert into PERMISSION values (1, 'utente1', 'pw1', 'admin');
insert into PERMISSION values (2, 'utente2', 'pw2', 'first');
insert into PERMISSION values (3, 'utente3', 'pw3', 'second');
insert into PERMISSION values (4, 'utente4', 'pw4', 'second');

insert into OPERATIONS values ('admin', 'getUsers');
insert into OPERATIONS values ('admin', 'getUserById');
insert into OPERATIONS values ('admin', 'deleteById');
insert into OPERATIONS values ('admin', 'newUser');
insert into OPERATIONS values ('admin', 'updateEta');
insert into OPERATIONS values ('first', 'getUserById');
insert into OPERATIONS values ('first', 'getUsers');
insert into OPERATIONS values ('first', 'newUser');
insert into OPERATIONS values ('second', 'getUserById');
insert into OPERATIONS values ('second', 'getUsers');
