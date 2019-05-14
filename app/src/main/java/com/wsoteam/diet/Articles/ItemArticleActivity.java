package com.wsoteam.diet.Articles;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wsoteam.diet.R;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlResImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemArticleActivity extends AppCompatActivity {

    @BindView(R.id.textView126) TextView textView;
    @BindView(R.id.html_text) HtmlTextView htmlTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_article);
        ButterKnife.bind(this);

        final String HTML = "<html>\n" +
                "<head>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p style=\"text-align: center;\"><span style=\"color: #bdc3c7;\">&rarr; This is a full-featured editor demo. Please explore! &larr;</span></p>\n" +
                "<p style=\"text-align: center;\">&nbsp;</p>\n" +
                "<h2 style=\"text-align: center;\">TinyMCE provides a <span style=\"text-decoration: underline;\">full-featured</span> rich text editing experience, and a featherweight download.</h2>\n" +
                "<p style=\"text-align: center;\"><strong> <span style=\"font-size: 14pt;\"> <span style=\"color: #7e8c8d; font-weight: 600;\">No matter what you're building, TinyMCE has got you covered.</span> </span> </strong></p>\n" +
                "<p style=\"text-align: center;\">&nbsp;</p>\n" +
                "<table style=\"border-collapse: collapse; width: 85%; margin-left: auto; margin-right: auto; border: 0;\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td style=\"width: 25%; text-align: center; padding: 7px;\"><span style=\"color: #95a5a6;\">\uD83D\uDEE0 50+ Plugins</span></td>\n" +
                "<td style=\"width: 25%; text-align: center; padding: 7px;\"><span style=\"color: #95a5a6;\">\uD83D\uDCA1 Premium Support</span></td>\n" +
                "<td style=\"width: 25%; text-align: center; padding: 7px;\"><span style=\"color: #95a5a6;\">\uD83D\uDD8D Custom Skins</span></td>\n" +
                "<td style=\"width: 25%; text-align: center; padding: 7px;\"><span style=\"color: #95a5a6;\">⚙ Full API Access</span></td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<img src=\"https://www.freelogodesign.org/Content/img/logo-ex-7.png\" >" +
                "</body>\n" +
                "</html>";

        final String html_line = "<html>\n" +
                " <head>\n" +
                "  <meta charset=\"utf-8\">\n" +
                "  <title>Цвет горизонтальной линии</title>\n" +
                "  <style>\n" +
                "   hr {\n" +
                "    border: none; /* Убираем границу */\n" +
                "    background-color: red; /* Цвет линии */\n" +
                "    color: red; /* Цвет линии для IE6-7 */\n" +
                "    height: 2px; /* Толщина линии */\n" +
                "   }\n" +
                "  </style>\n" +
                " </head>\n" +
                " <body>\n" +
                "  <hr>\n" +
                "  <p>Текстовоя строка</p>\n" +
                "  <hr>\n" +
                " </body>\n" +
                "</html>";

        final String simple_html = "<html>\n" +
                "<head>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Как составить план питания?</h1>\n" +
                "<p>У плана питания есть ряд преимуществ и недостатков, но плюсы определённо перевешивают. Смотрите сами: вы экономите время и деньги, меньше стрессуете, у вас есть стабильность в рационе и гарантия результата. Приятно? Конечно. Так давайте составим план питания, не откладывая!</p>\n" +
                "<h4>1. Рассчитайте вашу норму калорий и количество приёмов пищи.</h4>\n" +
                "<p>Учтите рост, вес, возраст, физическую нагрузку. Будьте честны с собой.<br />Что касается приёмов пищи, то оптимальное количество &mdash; от 3 до 5 раз в день вне зависимости от того, набираете вы вес или худеете.</p>\n" +
                "<hr />\n" +
                "<p>Необходимо чтобы потребность в энергии и энергетическая ценность рациона совпадали</p>\n" +
                "<hr />\n" +
                "<p>&nbsp;</p>\n" +
                "</body>\n" +
                "</html>";

////        textView.setText(Html.fromHtml(HTML));
//        textView.setVisibility(View.GONE);
//        htmlTextView.setHtml(HTML,
//                new HtmlResImageGetter(htmlTextView));



// loads html from string and displays http://www.example.com/cat_pic.png from the Internet
        htmlTextView.setHtml(simple_html,
                new HtmlHttpImageGetter(htmlTextView));
    }
}
