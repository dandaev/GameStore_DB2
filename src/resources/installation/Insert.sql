INSERT INTO [Publisher] ([PublisherName]) VALUES ('Techland') Go
INSERT INTO [Publisher] ([PublisherName]) VALUES ('CD PROJEKT RED') Go

INSERT INTO [Kategorie] ([KatName]) VALUES ('6+') Go
INSERT INTO [Kategorie] ([KatName]) VALUES ('12+') Go
INSERT INTO [Kategorie] ([KatName]) VALUES ('16+') Go
INSERT INTO [Kategorie] ([KatName]) VALUES ('18+') Go
INSERT INTO [Kategorie] ([KatName]) VALUES ('No Kategorie') Go

INSERT INTO [Spiel] ( [SpName], [Beschriebung], [Publikationsdatum], [PublisherNr], [KategorieNr], [SpPreis], [SpImage]) VALUES ('Dying Light 2 Stay Human', 'Vor mehr als zwanzig Jahren haben wir in Harran gegen das Virus gekämpft … und verloren. Jetzt verlieren wir wieder. Die Stadt wird von Konflikten zerfressen. Die Zivilisation wurde ins Mittelalter zurückversetzt. Und dennoch gibt es Hoffnung.
Sie sind ein Wanderer mit der Macht, die Stadt zu verändern. Aber Ihre Fähigkeiten haben einen Preis. Sie werden von Erinnerungen geplagt und haben sich aufgemacht, um die Wahrheit zu finden ... nur um mitten in ein Kriegsgebiet zu geraten. Arbeiten Sie an Ihren Fähigkeiten, denn um ihre Feinde zu besiegen und neue Verbündete zu gewinnen, werden Sie sowohl Ihre Fäuste als auch Ihren Verstand brauchen. Lüften Sie die dunklen Geheimnisse Ihrer Macht, wählen Sie eine Seite und bestimmen Sie über Ihr Schicksal. 
Und vergessen Sie dabei nicht, menschlich zu bleiben.', '12.07.2020', 1, 1, 59.99, 'EGS_DyingLight2StayHuman_Techland_S3_2560x1440-f1dcd15207f091674615ccb4bd9dc3c7.jpg'); Go
INSERT INTO [Spiel] ( [SpName], [Beschriebung], [Publikationsdatum], [PublisherNr], [KategorieNr], [SpPreis], [SpImage]) VALUES ('The Witcher® 3: Wild Hunt', 'The Witcher: Wild Hunt ist ein Rollenspiel mit packender Story und offener Welt in einem grafisch atemberaubenden Fantasy-Universum voller folgenreicher Entscheidungen und einschneidender Konsequenzen. In The Witcher spielst du den professionellen Monsterjager Geralt von Riva, dessen Aufgabe es ist, in einer riesigen offenen Welt voller Handelsstadte, 
Pirateninseln, gefahrlicher Gebirgspasse und vergessener Hohlen das Kind der Prophezeiung zu finden.', '18.05.2015', 2, 4, 19.99, 'maxresdefaultWitcher3WildHunt.jpg'); Go

INSERT INTO [Genre] (GeName) VALUES ('Aktion'); Go
INSERT INTO [Genre] (GeName) VALUES ('RPG'); Go
INSERT INTO [Genre] (GeName) VALUES ('Open World'); Go
INSERT INTO [Genre] (GeName) VALUES ('Magie'); Go

INSERT INTO [SpielGenres] ([SpielNr], [GenreNr]) VALUES (1,1); Go
INSERT INTO [SpielGenres] ([SpielNr], [GenreNr]) VALUES (1,2); Go

INSERT INTO [SpielGenres] ([SpielNr], [GenreNr]) VALUES (2,1); 
Go
INSERT INTO [SpielGenres] ([SpielNr], [GenreNr]) VALUES (2,2);
Go
INSERT INTO [SpielGenres] ([SpielNr], [GenreNr]) VALUES (2,3);
Go
INSERT INTO [SpielGenres] ([SpielNr], [GenreNr]) VALUES (2,4);
Go

INSERT INTO [Kommentar]
           ([KommText]
           ,[KommSpiel]
           ,[KommNutzer])
     VALUES
           ( 'Best Spiel dieser Genres, Ich bin sehr zufrieden und empfehle jedem'
           ,1
           ,6 )
GO
