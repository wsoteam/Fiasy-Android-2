package com.wsoteam.diet.Articles.POJO;

import java.util.ArrayList;
import java.util.List;

public class Factory {

    public static Article getArticle(){

        String imgUrl = "https://firebasestorage.googleapis.com/v0/b/diet-for-test.appspot.com/o/PlansRecipes%2F25.jpg?alt=media&token=20408376-144a-4a0e-b525-d0d3d4217318";
        String title = "<h1><span style=\"color: #000000;\">Как составить план питания?</span></h1>";
        String intro = "<p> У плана питания есть ряд преимуществ и недостатков, но плюсы определённо перевешивают. Смотрите сами: вы экономите время и деньги, меньше стрессуете, у вас есть стабильность в рационе и гарантия результата. Приятно? Конечно. Так давайте составим план питания, не откладывая!\n</p>\n";
        String main = "<p dir=\"ltr\"><span style=\"color: #000000;\"><strong>1. Рассчитайте вашу норму калорий и количество приёмов пищи.</strong></span></p>\n" +
                "<p dir=\"ltr\">Учтите рост, вес, возраст, физическую нагрузку. Будьте честны с собой.<br />Что касается приёмов пищи, то оптимальное количество &mdash; от 3 до 5 раз в день вне зависимости от того, набираете вы вес или худеете.</p>\n" +
                "<hr />\n" +
                "<p dir=\"ltr\" style=\"text-align: center;\"><em>Необходимо чтобы потребность в энергии и энергетическая ценность рациона совпадали</em></p>\n" +
                "<hr />\n" +
                "<p dir=\"ltr\"><span style=\"color: #000000;\"><strong>2. Разбейте пищу на БЖУ.</strong></span></p>\n" +
                "<p dir=\"ltr\">Примерно 50% рациона составляйте из медленных углеводов (макароны твёрдых сортов, бурый рис). А вот потребность в белке может варьироваться от 1 г на 1 кг веса при отсутствии спортивной нагрузки до 1,8 г на 1 кг веса при силовых тренировках. Самое большое количество белка нужно тем, кто &laquo;сушится&raquo; (снижает процент жира) &mdash; до 2,5 г на 1 кг в день.</p>\n" +
                "<hr />\n" +
                "<p dir=\"ltr\" style=\"text-align: center;\"><em>Принято считать, что в организме человека 1 г белка приводит к образованию 4 ккал энергии, 1 г жира &ndash; 9 ккал, 1 г углеводов &ndash; 4 ккал, а 1 г этанола &ndash; 7 ккал.</em></p>\n" +
                "<hr />\n" +
                "<p dir=\"ltr\"><span style=\"color: #000000;\"><strong>3. Составьте план по принципу &laquo;1/5 рациона любимой еды и 4/5 &mdash; полезной&raquo;.</strong></span></p>\n" +
                "<p dir=\"ltr\">Это значит 80% продуктов в рационе &mdash; это мясо, рыба, овощи, фрукты, орехи, молочные продукты. А остальные 20% &mdash; совершенно любые вредности, которые вы обожаете. Именно благодаря этому хитрому подходу риск сорваться стремится к нулю.</p>\n" +
                "<p dir=\"ltr\"><span style=\"color: #000000;\"><strong>4. Чит-мил или день &laquo;не по плану&raquo;.</strong></span></p>\n" +
                "<p dir=\"ltr\">Это, например, пицца в честь дня рождения. Помните, что 1 такой день не срывает все ваши планы. Просто продолжайте двигаться дальше.</p>\n" +
                "<hr />\n" +
                "<p dir=\"ltr\" style=\"text-align: center;\"><em>Читмил - это не просто праздник живота, а мероприятие, которое делает человека ближе к поставленной цели.</em></p>\n" +
                "<hr />\n" +
                "<p dir=\"ltr\"><span style=\"color: #000000;\"><strong>5. Гибкий план питания.</strong></span></p>\n" +
                "<p dir=\"ltr\">На этом этапе вы уже знаете, чем и как питаться, чтобы поддерживать вес в нужной форме. Вы отходите от жёстких рамок к продуманному и сбалансированному питанию.</p>\n" +
                "<p dir=\"ltr\">Составляйте план так, чтобы ваше меню было не только полезным и диетическим, но и вкусным и разнообразным. Худейте в своё удовольствие!</p>";


        Article article = new Article();
        article.setPremium(false);
        article.setImgUrl(imgUrl);
        article.setTitle(title);
        article.setIntroPart(intro);
        article.setMainPart(main);
        return article;
    }

    public static List<Article> getListArticles(){
        List<Article> list = new ArrayList<>();
        list.add(getArticle());
        list.add(getArticle());
        list.add(getArticle());

        return list;
    }

    public static ListArticles getObjectForFirebase(){
        ListArticles listArticles = new ListArticles();

        listArticles.setName("ARTICLES");
        listArticles.setListArticles(getListArticles());

        return listArticles;
    }
}
