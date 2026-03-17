package com.example.luxlexicaapp.data;

import android.content.Context;
import com.example.luxlexicaapp.data.entities.*;
import com.google.gson.Gson;
import java.util.Arrays;
import java.util.List;

public class DataInitializer {

    public static void initializeData(Context context, AppDatabase db) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            if (db.userDao().getMainUserSync() != null) return;

            Gson gson = new Gson();
            User mainUser = new User("Ученик");
            db.userDao().insert(mainUser);

            db.appDao().insertLanguage(new Language("en", "English"));
            db.appDao().insertLanguage(new Language("es", "Español"));
            db.appDao().insertLanguage(new Language("pt", "Português"));

            List<String> levels = Arrays.asList("A1", "A2", "B1", "B2", "C1");
            for (String l : levels) db.appDao().insertLevel(new Level(l));

            addEnglishModules(db, gson);
            addSpanishModules(db, gson);
            addPortugueseModules(db, gson);

            db.appDao().insertAchievement(new Achievement("Новичок", "Пройдено 5 уроков", "lessons_5", "ic_award_beginner"));
            db.appDao().insertAchievement(new Achievement("Эрудит", "50 правильных ответов", "correct_50", "ic_award_erudite"));
            db.appDao().insertAchievement(new Achievement("Постоянство", "Стрик 7 дней", "streak_7", "ic_award_streak"));

            seedDailyTasks(db);

            db.userDao().insert(createMockUser("Alice", 1500, 5));
            db.userDao().insert(createMockUser("Bob", 1200, 3));
            db.userDao().insert(createMockUser("Charlie", 2500, 10));
            db.userDao().insert(createMockUser("Diana", 1800, 4));
            db.userDao().insert(createMockUser("Ethan", 950, 2));
            db.userDao().insert(createMockUser("Fiona", 2100, 8));
            db.userDao().insert(createMockUser("George", 1350, 6));
            db.userDao().insert(createMockUser("Hannah", 1100, 1));
            db.userDao().insert(createMockUser("Ivan", 1650, 7));
            db.userDao().insert(createMockUser("Julia", 1400, 3));
        });
    }

    public static void seedDailyTasks(AppDatabase db) {
        db.dailyTaskDao().deleteAll();
        db.dailyTaskDao().insert(new DailyTask("Привет, новый день! (Зайти в приложение)", 50, "LOGIN", 1));
        db.dailyTaskDao().insert(new DailyTask("Первые шаги (Пройти любой урок)", 100, "ANY_LESSON", 1));
        db.dailyTaskDao().insert(new DailyTask("Мастер практики (Выполнить 3 практики)", 100, "PRACTICE", 3));
        db.dailyTaskDao().insert(new DailyTask("Эксперт тестов (Пройти 2 теста)", 100, "TEST", 2));
        db.dailyTaskDao().insert(new DailyTask("Любознательный ученик (Прочитать теорию)", 100, "THEORY", 1));
        db.dailyTaskDao().insert(new DailyTask("Полиглот (мини) (Уроки на 2 разных языках)", 100, "LANG_2", 2));
        db.dailyTaskDao().insert(new DailyTask("Энергичный ученик (Пройти 5 уроков)", 100, "TOTAL_5", 5));
        db.dailyTaskDao().insert(new DailyTask("Фокус на одном (3 урока на одном языке)", 100, "FOCUS_1", 3));
        db.dailyTaskDao().insert(new DailyTask("Настоящий полиглот (Задействовать 3 языка)", 100, "ALL_LANGS", 3));
        db.dailyTaskDao().insert(new DailyTask("Гармоничное развитие (Теория + Практика + Тест)", 100, "HARMONY", 3));
    }

    private static void addEnglishModules(AppDatabase db, Gson gson) {
        addModule(db, gson, 1, 1, "Greetings", "Verb 'to be' (am/is/are).", "Hello, ____ name is John. I ____ a student.", Arrays.asList("my / am", "his / is", "your / are"), 0, "Как переводится 'Привет'?", Arrays.asList("Goodbye", "Hello", "Thank you", "Yes"), 1);
        addModule(db, gson, 1, 1, "Numbers", "Numbers 1-20.", "I have ____ (5) apples.", Arrays.asList("five", "ten", "one"), 0, "Цифра 3?", Arrays.asList("Tree", "Three", "Free"), 1);
        addModule(db, gson, 1, 1, "My Family", "Family members.", "This is my ____. Her name is Sarah.", Arrays.asList("aunt", "uncle", "cousin"), 0, "Как перевести 'брат'?", Arrays.asList("Sister", "Father", "Brother"), 2);
        addModule(db, gson, 1, 1, "Everyday Objects", "Articles (a/an).", "It's ____ book.", Arrays.asList("a", "an", "the"), 0, "Яблоко во мн. числе?", Arrays.asList("Apple", "Apples", "An apple"), 1);
        addModule(db, gson, 1, 1, "Colors", "Basic adjectives.", "I have a ____ car.", Arrays.asList("red", "run", "blue"), 0, "Что значит 'большой'?", Arrays.asList("Small", "Big", "Cold"), 1);
        addModule(db, gson, 1, 2, "Past Simple (To Be)", "Was/were.", "I ____ at home yesterday.", Arrays.asList("was", "were", "am"), 0, "Они были?", Arrays.asList("They was", "They were", "They is"), 1);
        addModule(db, gson, 1, 2, "Past Simple (Regular)", "Formation (-ed).", "Last night, I ____ (watch) a movie.", Arrays.asList("watched", "watch", "watching"), 0, "Play в прошедшем?", Arrays.asList("Played", "Plays", "Play"), 0);
        addModule(db, gson, 1, 2, "Restaurant", "Ordering food.", "I ____ like to have a pizza.", Arrays.asList("would", "will", "do"), 0, "Меню?", Arrays.asList("Give me menu", "I want menu", "Can I have the menu please?"), 2);
        addModule(db, gson, 1, 2, "Directions", "Imperatives.", "Go straight ____.", Arrays.asList("ahead", "on", "quickly"), 0, "Turn right?", Arrays.asList("Поверни налево", "Иди прямо", "Поверни направо"), 2);
        addModule(db, gson, 1, 2, "Comparatives", "Cheaper, faster.", "This car is ____ (fast) than that one.", Arrays.asList("faster", "fastest", "more fast"), 0, "Мой дом больше?", Arrays.asList("My house is bigger", "My house more big"), 0);
        addModule(db, gson, 1, 3, "Present Perfect", "Have/has + P.P.", "Have you ____ been to Paris?", Arrays.asList("ever", "never", "yet"), 0, "Я никогда не был?", Arrays.asList("I never been", "I have never been"), 1);
        addModule(db, gson, 1, 3, "PP vs Past Simple", "Experience.", "I ____ (see) that movie last week.", Arrays.asList("saw", "have seen"), 0, "Past Simple?", Arrays.asList("I went in 2010", "I have gone"), 0);
        addModule(db, gson, 1, 3, "Predictions", "Will.", "Don't worry, I ____ help you.", Arrays.asList("will", "going to"), 0, "Спонтанное решение?", Arrays.asList("I'm going to call", "I will call"), 1);
        addModule(db, gson, 1, 3, "Social Media", "Phrasal verbs.", "If I ____ (have) more free time, I ____ (start) a blog.", Arrays.asList("had / would start", "have / will start"), 0, "log in?", Arrays.asList("Выйти", "Войти"), 1);
        addModule(db, gson, 1, 4, "Reporting Verbs", "Suggest, deny.", "He denied ____ (steal) the money.", Arrays.asList("stealing", "to steal"), 0, "He said: 'I didn't do it'", Arrays.asList("He denied doing it", "He said he didn't"), 0);
        addModule(db, gson, 1, 4, "Inversion", "Emphasis.", "Never ____ I seen such a sunset.", Arrays.asList("have", "had", "did"), 0, "Никогда я не был...", Arrays.asList("Never was I so happy", "Never I was"), 0);
        addModule(db, gson, 1, 4, "Mixed Conditionals", "Hypothetical.", "If I ____ (take) that job, I ____ (be) rich now.", Arrays.asList("had taken / would be", "took / would be"), 0, "Если бы выучил...", Arrays.asList("If I had learned", "If I learned"), 0);
        addModule(db, gson, 1, 4, "Subjunctive", "Formal.", "I insist that he ____ (arrive) on time.", Arrays.asList("arrive", "arrives"), 0, "Формальный вариант?", Arrays.asList("I suggest he go", "I suggest he goes"), 0);
        addModule(db, gson, 1, 5, "Personality", "Advanced.", "He is so ____ (pays attention).", Arrays.asList("meticulous", "careless"), 0, "Gullible?", Arrays.asList("Умный", "Легко обманываемый"), 1);
    }

    private static void addSpanishModules(AppDatabase db, Gson gson) {
        addModule(db, gson, 2, 1, "Saludos", "Ser.", "Hola, me ____ Juan.", Arrays.asList("llamo", "llama"), 0, "Привет?", Arrays.asList("Hola", "Adiós"), 0);
        addModule(db, gson, 2, 1, "Números", "1-30.", "Tengo ____ (20) años.", Arrays.asList("veinte", "diez"), 0, "Цифра 5?", Arrays.asList("Cinco", "Cuatro"), 0);
        addModule(db, gson, 2, 1, "Mi Familia", "Posesivos.", "Mi ____ se llama Ana.", Arrays.asList("tía", "prima"), 0, "Брат?", Arrays.asList("Hermano", "Padre"), 0);
        addModule(db, gson, 2, 1, "En la Clase", "Artículos.", "____ libro es interesante.", Arrays.asList("El", "La"), 0, "Plural ventana?", Arrays.asList("Las ventanas", "La ventana"), 0);
        addModule(db, gson, 2, 1, "Colores", "Rojo.", "Casa ____ (blanca).", Arrays.asList("blanca", "blanco"), 0, "Маленький?", Arrays.asList("Pequeño", "Grande"), 0);
        addModule(db, gson, 2, 2, "Rutina", "Reflexivos.", "Yo ____ (levantarse) a las 7.", Arrays.asList("me levanto", "levanto"), 0, "Я встаю?", Arrays.asList("Me levanto", "Me ducho"), 0);
        addModule(db, gson, 2, 2, "Indefinido", "Ayer.", "Ayer ____ (comprar) un libro.", Arrays.asList("compré", "compro"), 0, "Pasado hablar (yo)?", Arrays.asList("Hablé", "Habló"), 0);
        addModule(db, gson, 2, 2, "Restaurante", "Pedir.", "De primero, ____ (querer).", Arrays.asList("quiero", "traigo"), 0, "Счет?", Arrays.asList("¿La cuenta?", "¿El menú?"), 0);
        addModule(db, gson, 2, 2, "Direcciones", "Imperativo.", "Gira a la ____ (left).", Arrays.asList("izquierda", "derecha"), 0, "Gira a la izquierda?", Arrays.asList("Налево", "Направо"), 0);
        addModule(db, gson, 2, 2, "Comparativos", "Más... que.", "El coche es ____ rápido.", Arrays.asList("más", "tan"), 0, "Дом больше?", Arrays.asList("Mi casa es más grande", "Mi casa grande"), 0);
        addModule(db, gson, 2, 3, "Rutinas B1", "Frequência.", "Salgo con amigos.", Arrays.asList("salgo", "salir"), 0, "Casi nunca?", Arrays.asList("Почти никогда", "Часто"), 0);
        addModule(db, gson, 2, 3, "Comida", "Recetas.", "Corta las patatas.", Arrays.asList("corta", "cortar"), 0, "Приготовь?", Arrays.asList("Prepáralo", "Prepararlo"), 0);
        addModule(db, gson, 2, 3, "Experiencias", "PP.", "¿Has probado?", Arrays.asList("probado", "probar"), 0, "Уже?", Arrays.asList("Ya", "Todavía"), 0);
        addModule(db, gson, 2, 3, "Biografías", "Nacer.", "Nació en 1907.", Arrays.asList("nació", "nacía"), 0, "Ir (él)?", Arrays.asList("Fue", "Va"), 0);
        addModule(db, gson, 2, 4, "Emociones", "Subjuntivo.", "Me alegro de que ____.", Arrays.asList("vengas", "vienes"), 0, "Me alegra que...", Arrays.asList("Subjuntivo", "Indicativo"), 0);
        addModule(db, gson, 2, 4, "Opiniones", "Negativo.", "No creo que ____ razón.", Arrays.asList("tengan", "tienen"), 0, "No creo que...", Arrays.asList("venga", "viene"), 0);
        addModule(db, gson, 2, 4, "Condicionais", "Si + Imperf.", "Si ____ dinero, viajaría.", Arrays.asList("tuviera", "tenía"), 0, "Если бы я был...", Arrays.asList("Si estuviera", "Si estoy"), 0);
        addModule(db, gson, 2, 4, "Cultura", "Tradiciones.", "Celebran Día de Muertos.", Arrays.asList("celebran", "celebrar"), 0, "Обычай?", Arrays.asList("Costumbre", "Cultura"), 0);
        addModule(db, gson, 2, 5, "Creatividad", "Absurdo.", "Si fuera un pájaro.", Arrays.asList("fuera", "soy"), 0, "Subjuntivo?", Arrays.asList("-ra", "-ría"), 0);
    }

    private static void addPortugueseModules(AppDatabase db, Gson gson) {
        addModule(db, gson, 3, 1, "Saudações", "Ser.", "Olá, eu ____ Maria.", Arrays.asList("me chamo", "se chama"), 0, "Здравствуйте?", Arrays.asList("Olá", "Tchau"), 0);
        addModule(db, gson, 3, 1, "Números", "1-30.", "Tenho ____ (20) anos.", Arrays.asList("vinte", "dez"), 0, "Цифра 4?", Arrays.asList("Quatro", "Cinco"), 0);
        addModule(db, gson, 3, 1, "Família", "Vocabulário.", "Minha ____ é a Ana.", Arrays.asList("tia", "avó"), 0, "Брат?", Arrays.asList("Irmão", "Irmã"), 0);
        addModule(db, gson, 3, 1, "Sala", "Artigos.", "____ libro é novo.", Arrays.asList("O", "A"), 0, "Plural janela?", Arrays.asList("As janelas", "Os janelas"), 0);
        addModule(db, gson, 3, 1, "Cores", "Adjetivos.", "Casa ____ (branca).", Arrays.asList("branca", "branco"), 0, "Маленький?", Arrays.asList("Pequeno", "Grande"), 0);
        addModule(db, gson, 3, 2, "Rotina", "Levantar-se.", "Eu ____ às 7.", Arrays.asList("me levanto", "levanto"), 0, "Я встаю?", Arrays.asList("Me levanto", "Me deito"), 0);
        addModule(db, gson, 3, 2, "Pretérito", "Passado.", "Ontem ____ um livro.", Arrays.asList("comprei", "compro"), 0, "Falar (eu)?", Arrays.asList("Falei", "Falou"), 0);
        addModule(db, gson, 3, 2, "No Restaurante", "Pedir.", "____ uma salada.", Arrays.asList("Quero", "Trago"), 0, "Счет?", Arrays.asList("A conta?", "O preço?"), 0);
        addModule(db, gson, 3, 2, "Direções", "Vire.", "Vire à ____ (left).", Arrays.asList("esquerda", "direita"), 0, "Vire à esquerda?", Arrays.asList("Налево", "Направо"), 0);
        addModule(db, gson, 3, 2, "Comparativos", "Mais.", "O carro é ____ rápido.", Arrays.asList("mais", "tão"), 0, "Casa maior?", Arrays.asList("Minha casa é maior", "Mais grande"), 0);
        addModule(db, gson, 3, 3, "Rotinas B1", "Frequência.", "Quase nunca vejo TV.", Arrays.asList("vejo", "ver"), 0, "Quase nunca?", Arrays.asList("Почти nunca", "Sempre"), 0);
        addModule(db, gson, 3, 3, "Comida", "Receitas.", "Corte as batatas.", Arrays.asList("corte", "cortar"), 0, "Приготовь?", Arrays.asList("Prepare isso", "Preparar"), 0);
        addModule(db, gson, 3, 3, "Experiências", "PP.", "Você já provou?", Arrays.asList("provou", "prova"), 0, "Уже?", Arrays.asList("Já", "Ainda"), 0);
        addModule(db, gson, 3, 3, "Biografias", "Pessoa.", "Nasceu em 1888.", Arrays.asList("Nasceu", "Nascia"), 0, "Imperfeito?", Arrays.asList("Descрипções", "Ações concluídas"), 0);
        addModule(db, gson, 3, 4, "Emoções", "Conjuntivo.", "Alegro-me que ____.", Arrays.asList("venha", "vem"), 0, "É uma pena que...", Arrays.asList("Conjuntivo", "Indicativo"), 0);
        addModule(db, gson, 3, 4, "Opiniones", "Não acho.", "Não acho que ____ razão.", Arrays.asList("tenham", "têm"), 0, "Não acho que...", Arrays.asList("venha", "vem"), 0);
        addModule(db, gson, 3, 4, "Condicionais", "Se.", "Se ____ dinheiro, viajaría.", Arrays.asList("tivesse", "tinha"), 0, "Se eu fosse...", Arrays.asList("Se fosse", "Se sou"), 0);
        addModule(db, gson, 3, 4, "Cultura", "Carnaval.", "Comemoram Carnaval.", Arrays.asList("comemoram", "comemorar"), 0, "Обычай?", Arrays.asList("Costume", "Personalidade"), 0);
        addModule(db, gson, 3, 5, "Criatividade", "Conjuntivo.", "Se fosse un pájaro.", Arrays.asList("fosse", "sou"), 0, "Hipotéticas?", Arrays.asList("Imperfeito Conj.", "Presente"), 0);
    }

    private static void addModule(AppDatabase db, Gson gson, int langId, int levelId, String name, String theory, 
                                 String practiceQ, List<String> practiceOpts, int practiceCorrect,
                                 String testQ, List<String> testOpts, int testCorrect) {
        Module module = new Module(langId, levelId, name, theory);
        long moduleId = db.appDao().insertModule(module);
        db.appDao().insertLesson(new Lesson((int)moduleId, theory));
        long testId = db.appDao().insertTest(new Test((int)moduleId));
        db.appDao().insertQuestion(new Question((int)testId, testQ, gson.toJson(testOpts), testCorrect));
        db.appDao().insertPracticeQuestion(new PracticeQuestion((int)moduleId, practiceQ, gson.toJson(practiceOpts), practiceCorrect));
    }

    private static User createMockUser(String name, int exp, int streak) {
        User u = new User(name);
        u.experience = exp;
        u.streak = streak;
        return u;
    }
}
