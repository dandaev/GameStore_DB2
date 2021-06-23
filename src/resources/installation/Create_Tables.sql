CREATE TABLE [Spiel] (
  [SpNr] integer PRIMARY KEY IDENTITY(1, 1),
  [SpName] varchar(255),
  [Beschriebung] text,
  [Publikationsdatum] date,
  [PublisherNr] integer,
  [KategorieNr] integer,
  [SpPreis] decimal(7,2),
  [SpImage] varchar(255),
  [LinkZumDownload] varchar(255)
)
GO

CREATE TABLE [Nutzer] (
  [NutzNr] integer PRIMARY KEY IDENTITY(1, 1),
  [NutzName] varchar(100),
  [NutzLogin] varchar(50),
  [NutzPasswort] varchar(50),
  [NutzRole] nvarchar(255) NOT NULL CHECK ([NutzRole] IN ('Reader', 'ReadWriter', 'Admin'))
)
GO

CREATE TABLE [Kommentar] (
  [KommNr] integer PRIMARY KEY IDENTITY(1, 1),
  [KommText] text,
  [KommSpiel] integer,
  [KommNutzer] integer
)
GO

CREATE TABLE [Genre] (
  [GeNr] integer PRIMARY KEY IDENTITY(1, 1),
  [GeName] varchar(30)
)
GO

CREATE TABLE [Kategorie] (
  [KatNr] integer PRIMARY KEY IDENTITY(1, 1),
  [KatName] varchar(30)
)
GO

CREATE TABLE [Publisher] (
  [PubNr] integer PRIMARY KEY IDENTITY(1, 1),
  [PublisherName] varchar(50)
)
GO

CREATE TABLE [NutzerSpiels] (
  [NutzerNr] integer,
  [SpielNr] integer
)
GO

CREATE TABLE [SpielGenres] (
  [SpielNr] integer,
  [GenreNr] integer
)
GO

ALTER TABLE [Spiel] ADD FOREIGN KEY ([PublisherNr]) REFERENCES [Publisher] ([PubNr])
GO

ALTER TABLE [Spiel] ADD FOREIGN KEY ([KategorieNr]) REFERENCES [Kategorie] ([KatNr])
GO

ALTER TABLE [Kommentar] ADD FOREIGN KEY ([KommSpiel]) REFERENCES [Spiel] ([SpNr])
GO

ALTER TABLE [Kommentar] ADD FOREIGN KEY ([KommNutzer]) REFERENCES [Nutzer] ([NutzNr])
GO

ALTER TABLE [NutzerSpiels] ADD FOREIGN KEY ([NutzerNr]) REFERENCES [Nutzer] ([NutzNr])
GO

ALTER TABLE [NutzerSpiels] ADD FOREIGN KEY ([SpielNr]) REFERENCES [Spiel] ([SpNr])
GO

ALTER TABLE [SpielGenres] ADD FOREIGN KEY ([SpielNr]) REFERENCES [Spiel] ([SpNr])
GO

ALTER TABLE [SpielGenres] ADD FOREIGN KEY ([GenreNr]) REFERENCES [Genre] ([GeNr])
GO
