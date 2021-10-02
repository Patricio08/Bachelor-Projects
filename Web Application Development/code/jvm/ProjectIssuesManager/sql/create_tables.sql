CREATE TABLE COLLABORATOR(
	name varchar(25),
	password varchar(25) NOT NULL,
	PRIMARY KEY (name)
);

CREATE TABLE PROJECT(
	name varchar(25),
	description varchar(256) NOT NULL,
	startState varchar(25) NOT NULL,
	PRIMARY KEY (name)
);

CREATE TABLE LABEL(
	labelName varchar(25),
	PRIMARY KEY (labelName)
);

CREATE TABLE PROJECTLABEL(
	projectName varchar(25),
	labelName varchar(25),
	PRIMARY KEY (projectName, labelName),
	FOREIGN KEY (projectName) REFERENCES PROJECT(name) ON DELETE CASCADE ON UPDATE CASCADE,
  	FOREIGN KEY (labelName) REFERENCES LABEL(labelName) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE STATE(
	stateName varchar(25),
	PRIMARY KEY (stateName)
);

CREATE TABLE PROJECTSTATE(
	projectName varchar(25),
	stateName varchar(25),
	PRIMARY KEY (projectName, stateName),
	FOREIGN KEY (projectName) REFERENCES PROJECT(name) ON DELETE CASCADE ON UPDATE CASCADE,
  	FOREIGN KEY (stateName) REFERENCES STATE(stateName) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE NEXTSTATE(
	projectName varchar(25),
	stateName varchar(25),
	nextState varchar(25),
	PRIMARY KEY (projectName, stateName, nextState),
	FOREIGN KEY (projectName, stateName) REFERENCES PROJECTSTATE (projectName, stateName) ON DELETE CASCADE ON UPDATE CASCADE,
  	FOREIGN KEY (projectName, nextState) REFERENCES PROJECTSTATE (projectName, stateName) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE ISSUE(
	projectName varchar(25),
	name varchar(25) NOT NULL,
	description varchar(256) NOT NULL,
	beginDate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	endDate timestamp,
	stateName varchar(25) NOT NULL,
	id SERIAL,
	PRIMARY KEY (projectName, id),
	FOREIGN KEY (projectName) REFERENCES PROJECT (name)  ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (projectName, stateName) REFERENCES PROJECTSTATE (projectName, stateName) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE ISSUELABEL(
	projectName varchar(25),
	id int,
	labelName varchar(25),
	PRIMARY KEY (projectName, id, labelName),
	FOREIGN KEY (projectName, id) REFERENCES ISSUE (projectName, id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (projectName, labelName) REFERENCES PROJECTLABEL (projectName, labelName) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE COMMENT(
	projectName varchar(25),
	issueId int,
	text varchar(256) NOT NULL,
	date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	id SERIAL,
	PRIMARY KEY (projectName, issueId, id),
	FOREIGN KEY (projectName, issueId) REFERENCES ISSUE (projectName, id)  ON DELETE CASCADE ON UPDATE CASCADE
);

