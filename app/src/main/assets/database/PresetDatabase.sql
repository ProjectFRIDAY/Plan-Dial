PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE `PresetTable` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `presetName` TEXT, `templateId` TEXT, `presetTimeUnit` TEXT, `presetTime` TEXT, `presetIcon` TEXT);

-- templete 1 = t1, 자취 템플릿
INSERT INTO PresetTable VALUES(1,'옷 세탁','t1','DAY',4,'icon_id');
INSERT INTO PresetTable VALUES(2,'화장실 청소','t1','WEEK',2,'icon_id');
INSERT INTO PresetTable VALUES(3,'방 청소','t1','DAY',3,'icon_id');
INSERT INTO PresetTable VALUES(4,'재활용','t1','DAY',2,'icon_id');
INSERT INTO PresetTable VALUES(5,'옷 정리','t1','DAY',4,'icon_id');

-- templete 2 = t2, 헬스 템플릿
INSERT INTO PresetTable VALUES(6,'가슴 & 삼두1','t2','WEEK',1,'icon_id');
INSERT INTO PresetTable VALUES(7,'가슴 & 삼두2','t2','WEEK',1,'icon_id');
INSERT INTO PresetTable VALUES(8,'등 & 이두1','t2','WEEK',1,'icon_id');
INSERT INTO PresetTable VALUES(9,'등 & 이두2','t2','WEEK',1,'icon_id');
INSERT INTO PresetTable VALUES(10,'다리 & 어깨1','t2','WEEK',1,'icon_id');
INSERT INTO PresetTable VALUES(11,'다리 & 어깨2','t2','WEEK',1,'icon_id');
INSERT INTO PresetTable VALUES(12,'물 마시기','t2','HOUR',1,'icon_id');
INSERT INTO PresetTable VALUES(13,'프로틴 섭취','t2','DAY',1,'icon_id');

-- templete 3 = t3, 수험생 템플릿
INSERT INTO PresetTable VALUES(14,'규칙적식생활','t3','DAY',1,'icon_id');
INSERT INTO PresetTable VALUES(15,'스트레칭','t3','HOUR',1,'icon_id');
INSERT INTO PresetTable VALUES(16,'비타민복용','t3','DAY',1,'icon_id');

-- templete 4 = t4, 건강 및 자기계발 템플릿
INSERT INTO PresetTable VALUES(17,'기상','t4','DAY',1,'icon_id');
INSERT INTO PresetTable VALUES(18,'비타민 & 약 복용','t4','HOUR',12,'icon_id');
INSERT INTO PresetTable VALUES(19,'전화영어','t4','DAY',1,'icon_id');
INSERT INTO PresetTable VALUES(20,'다이어리 작성','t4','DAY',1,'icon_id');
INSERT INTO PresetTable VALUES(21,'취침','t4','DAY',1,'icon_id');
INSERT INTO PresetTable VALUES(22,'독서','t4','HOUR',12,'icon_id');
INSERT INTO PresetTable VALUES(23,'취미','t4','DAY',1,'icon_id');

-- templete 5 = t5, 건강한 출퇴근 템플릿
INSERT INTO PresetTable VALUES(24,'새벽 홈 트레이닝','t5','DAY',1,'icon_id');
INSERT INTO PresetTable VALUES(25,'아침 독서','t5','DAY',1,'icon_id');
INSERT INTO PresetTable VALUES(26,'아침일기 작성(오늘의 목표)','t5','DAY',1,'icon_id');
INSERT INTO PresetTable VALUES(27,'아침 식사 준비','t5','DAY',1,'icon_id');
INSERT INTO PresetTable VALUES(28,'퇴근 후 계단운동','t5','DAY',1,'icon_id');
INSERT INTO PresetTable VALUES(29,'하루를 마치는 독서','t5','DAY',1,'icon_id');
INSERT INTO PresetTable VALUES(30,'하루를 마치는 일기','t5','DAY',1,'icon_id');

-- templete 카테고리 정보 저장
INSERT INTO PresetTable VALUES(31,'자취','t1','0',0,'0');
INSERT INTO PresetTable VALUES(32,'헬스','t2','0',0,'0');
INSERT INTO PresetTable VALUES(33,'수험생','t3','0',0,'0');
INSERT INTO PresetTable VALUES(34,'건강&자기개발','t4','0',0,'0');
INSERT INTO PresetTable VALUES(35,'건강한출퇴근','t5','0',0,'0');

COMMIT;