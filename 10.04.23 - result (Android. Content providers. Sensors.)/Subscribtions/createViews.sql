-- удаление представлений
drop view if exists view_subscriptions;
drop view if exists view_publications;

-- создание представления таблицы подписок

create view view_subscriptions as
select subscriptions._id,                 -- id подписки
       publications._id as publiation_id, -- id издания
       pub_type.type    as pub_type,      -- тип издания
       publications.pub_name,             -- название издания
       publications.unit_price,           -- стоимость 1 ед
       subscriptions.date_start,          -- дата начала
       subscriptions.duration             -- длительность
from subscriptions
         join (publications join publications_types pub_type on publications.type_id = pub_type._id)
              on subscriptions.publication_id = publications._id;


-- представление таблицы изданий
CREATE VIEW view_publications AS
select publications._id,                   -- id
       publications_types.type as pub_type,-- вид издания
       publications.pub_index,             -- индекс для подписки
       publications.pub_name,              -- названия издания
       publications.unit_price             -- стоимость единицы
from publications
         join publications_types on publications.type_id = publications_types._id;

