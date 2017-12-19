-- 고객
CREATE TABLE customer (
	id          VARCHAR(10) NOT NULL, -- 고객번호
	name        VARCHAR(20) NOT NULL, -- 이름
	address     VARCHAR(50) NOT NULL, -- 주소
	phoneNumber VARCHAR(20) NOT NULL  -- 핸드폰번호
);

-- 고객 기본키
CREATE UNIQUE INDEX PK_customer
	ON customer ( -- 고객
		id ASC -- 고객번호
	);

-- 고객
ALTER TABLE customer
	ADD
		CONSTRAINT PK_customer -- 고객 기본키
		PRIMARY KEY (
			id -- 고객번호
		);

-- 계좌정보
CREATE TABLE accountInfo (
	accountNumber VARCHAR(20) NOT NULL, -- 계좌번호
	code          VARCHAR(3)  NOT NULL, -- 은행코드
	bankbook      VARCHAR(10) NULL,     -- 통장구분
	password      VARCHAR(10) NULL,     -- 비밀번호
	balance       INTEGER     NULL,     -- 잔고
	id            VARCHAR(10) NULL      -- 고객번호
);

-- 계좌정보 기본키
CREATE UNIQUE INDEX PK_accountInfo
	ON accountInfo ( -- 계좌정보
		accountNumber ASC, -- 계좌번호
		code          ASC  -- 은행코드
	);

-- 계좌정보
ALTER TABLE accountInfo
	ADD
		CONSTRAINT PK_accountInfo -- 계좌정보 기본키
		PRIMARY KEY (
			accountNumber, -- 계좌번호
			code           -- 은행코드
		);

-- 거래내역
CREATE TABLE dealInfo (
	accountNumber VARCHAR(20) NOT NULL, -- 계좌번호
	code          VARCHAR(3)  NOT NULL, -- 은행코드
	dateTime      DATETIME    NOT NULL, -- 거래날짜
	dealFlag      VARCHAR(10) NOT NULL, -- 거래상태
	dealContent   VARCHAR(30) NULL,     -- 거래설명
	dealMoney     INTEGER     NOT NULL, -- 거래금액
	balance       INTEGER     NOT NULL  -- 잔고
);

-- 거래내역 기본키
CREATE UNIQUE INDEX PK_dealInfo
	ON dealInfo ( -- 거래내역
		accountNumber ASC, -- 계좌번호
		code          ASC  -- 은행코드
	);

-- 거래내역
ALTER TABLE dealInfo
	ADD
		CONSTRAINT PK_dealInfo -- 거래내역 기본키
		PRIMARY KEY (
			accountNumber, -- 계좌번호
			code           -- 은행코드
		);


-- 은행코드구분
CREATE TABLE BankCode (
	code VARCHAR(3)  NOT NULL, -- 은행코드
	name VARCHAR(10) NOT NULL  -- 은행명
);

-- 은행코드구분 기본키
CREATE UNIQUE INDEX PK_BankCode
	ON BankCode ( -- 은행코드구분
		code ASC -- 은행코드
	);

-- 은행코드구분
ALTER TABLE BankCode
	ADD
		CONSTRAINT PK_BankCode -- 은행코드구분 기본키
		PRIMARY KEY (
			code -- 은행코드
		);

-- 계좌정보
ALTER TABLE accountInfo
	ADD
		CONSTRAINT FK_customer_TO_accountInfo -- 고객 -> 계좌정보
		FOREIGN KEY (
			id -- 고객번호
		)
		REFERENCES customer ( -- 고객
			id -- 고객번호
		);

-- 계좌정보
ALTER TABLE accountInfo
	ADD
		CONSTRAINT FK_BankCode_TO_accountInfo -- 은행코드구분 -> 계좌정보
		FOREIGN KEY (
			code -- 은행코드
		)
		REFERENCES BankCode ( -- 은행코드구분
			code -- 은행코드
		);

-- 거래내역
ALTER TABLE dealInfo
	ADD
		CONSTRAINT FK_accountInfo_TO_dealInfo -- 계좌정보 -> 거래내역
		FOREIGN KEY (
			accountNumber, -- 계좌번호
			code           -- 은행코드
		)
		REFERENCES accountInfo ( -- 계좌정보
			accountNumber, -- 계좌번호
			code           -- 은행코드
		);
		

drop table customer;
drop table		accountInfo;
drop table		dealInfo;
drop table bankcode
		
		
insert into customer values('c1', 'namhyukmin', 'seoul', '010-4117-7501');
insert into customer values('c2', 'parkjihae', 'seoul', '010-1111-2222');
insert into customer values('c3', 'kimsungsu', 'sunae', '010-3906-9073');


insert into bankCode values('C00', 'EPOZEN');
insert into bankCode values('C01', 'KAKAO');
insert into bankCode values('C02', 'TOSS');
insert into bankCode values('C03', 'WORI');

insert into accountInfo values('1111', 'C00', 'insert', '1234', 100000, 'c1');
insert into accountInfo values('2222', 'C01', 'insert', '1234', 100000, 'c2');
insert into accountInfo values('3333', 'C02', 'insert', '1234', 200000, 'c3');

select *from customer;
select * from accountinfo;

update accountInfo set balance=(balance-10000) where accountNumber='1111'


select customer.name, accountInfo.balance from accountInfo, customer where password='1234' and accountNumber='1111' and accountInfo.customerID = customer.customerID


select accountNumber from accountInfo where accountNumber='1111'
select customer.name, accountInfo.balance from accountInfo, customer where password='1234' and accountInfo.customerID = customer.customerID;