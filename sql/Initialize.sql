USE [master]
GO
/****** Object:  Database [PartsCatalog]    Script Date: 2021-10-12 14:27:34 ******/
CREATE DATABASE [PartsCatalog]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'PartsCatalog', FILENAME = N'E:\PartsCatalog\MSSQL\Data\PartsCatalog.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB ), 
 FILEGROUP [Files] CONTAINS FILESTREAM  DEFAULT
( NAME = N'PartsCatalog_Files', FILENAME = N'E:\PartsCatalog\MSSQL\Files\PartsCatalog_Files' , MAXSIZE = UNLIMITED)
 LOG ON 
( NAME = N'PartsCatalog_log', FILENAME = N'E:\PartsCatalog\Logs\PartsCatalog_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT
GO
ALTER DATABASE [PartsCatalog] SET COMPATIBILITY_LEVEL = 110
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [PartsCatalog].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [PartsCatalog] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [PartsCatalog] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [PartsCatalog] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [PartsCatalog] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [PartsCatalog] SET ARITHABORT OFF 
GO
ALTER DATABASE [PartsCatalog] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [PartsCatalog] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [PartsCatalog] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [PartsCatalog] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [PartsCatalog] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [PartsCatalog] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [PartsCatalog] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [PartsCatalog] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [PartsCatalog] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [PartsCatalog] SET  DISABLE_BROKER 
GO
ALTER DATABASE [PartsCatalog] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [PartsCatalog] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [PartsCatalog] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [PartsCatalog] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [PartsCatalog] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [PartsCatalog] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [PartsCatalog] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [PartsCatalog] SET RECOVERY FULL 
GO
ALTER DATABASE [PartsCatalog] SET  MULTI_USER 
GO
ALTER DATABASE [PartsCatalog] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [PartsCatalog] SET DB_CHAINING OFF 
GO
ALTER DATABASE [PartsCatalog] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [PartsCatalog] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [PartsCatalog] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [PartsCatalog] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
EXEC sys.sp_db_vardecimal_storage_format N'PartsCatalog', N'ON'
GO
ALTER DATABASE [PartsCatalog] SET QUERY_STORE = OFF
GO
USE [PartsCatalog]
GO
/****** Object:  User [partcat]    Script Date: 2021-10-12 14:27:34 ******/
CREATE USER [partcat] FOR LOGIN [partscatalog] WITH DEFAULT_SCHEMA=[dbo]
GO
ALTER ROLE [db_owner] ADD MEMBER [partcat]
GO
/****** Object:  Table [dbo].[BillOfMaterialsItems]    Script Date: 2021-10-12 14:27:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BillOfMaterialsItems](
	[ItemID] [int] IDENTITY(1,1) NOT NULL,
	[Quantity] [int] NOT NULL,
	[RefDes] [text] NOT NULL,
	[Populate] [bit] NOT NULL,
	[ComponentID] [int] NULL,
	[ProjectID] [int] NOT NULL,
 CONSTRAINT [BillOfMaterials$ID] PRIMARY KEY CLUSTERED 
(
	[ItemID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Categories]    Script Date: 2021-10-12 14:27:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Categories](
	[CategoryID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [nvarchar](50) NOT NULL,
 CONSTRAINT [Categories$PrimaryKey] PRIMARY KEY CLUSTERED 
(
	[CategoryID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Components]    Script Date: 2021-10-12 14:27:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Components](
	[ComponentID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [nvarchar](100) NOT NULL,
	[Quantity] [bigint] NOT NULL,
	[Description] [text] NULL,
	[CategoryID] [int] NOT NULL,
	[SubCategoryID] [int] NOT NULL,
	[PackageID] [int] NOT NULL,
 CONSTRAINT [Components$ID] PRIMARY KEY CLUSTERED 
(
	[ComponentID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Datasheets]    Script Date: 2021-10-12 14:27:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Datasheets](
	[DatasheetID] [int] IDENTITY(1,1) NOT NULL,
	[FileContent] [varbinary](max) FILESTREAM  NOT NULL,
	[ComponentID] [int] NOT NULL,
	[rowguid] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
 CONSTRAINT [PK_Datasheets] PRIMARY KEY CLUSTERED 
(
	[DatasheetID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY] FILESTREAM_ON [Files],
 CONSTRAINT [UQ__Datashee__F73921F7D66A8295] UNIQUE NONCLUSTERED 
(
	[rowguid] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] FILESTREAM_ON [Files]
GO
/****** Object:  Table [dbo].[Images]    Script Date: 2021-10-12 14:27:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Images](
	[ImageID] [int] IDENTITY(1,1) NOT NULL,
	[Format] [varchar](10) NOT NULL,
	[rowguid] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
	[FileContent] [varbinary](max) FILESTREAM  NOT NULL,
	[ComponentID] [int] NULL,
	[PackageID] [int] NULL,
 CONSTRAINT [PK_Images] PRIMARY KEY CLUSTERED 
(
	[ImageID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY] FILESTREAM_ON [Files],
UNIQUE NONCLUSTERED 
(
	[rowguid] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] FILESTREAM_ON [Files]
GO
/****** Object:  Table [dbo].[Packages]    Script Date: 2021-10-12 14:27:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Packages](
	[PackageID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [nvarchar](30) NOT NULL,
 CONSTRAINT [Packages$ID] PRIMARY KEY CLUSTERED 
(
	[PackageID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Projects]    Script Date: 2021-10-12 14:27:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Projects](
	[ProjectID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [nvarchar](70) NOT NULL,
	[Revision] [nvarchar](15) NULL,
	[Description] [text] NULL,
 CONSTRAINT [Projects$ID] PRIMARY KEY CLUSTERED 
(
	[ProjectID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Properties]    Script Date: 2021-10-12 14:27:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Properties](
	[PropertyID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [nvarchar](255) NULL,
	[Value] [nvarchar](255) NULL,
	[ComponentID] [int] NOT NULL,
 CONSTRAINT [Properties$ID] PRIMARY KEY CLUSTERED 
(
	[PropertyID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[SubCategories]    Script Date: 2021-10-12 14:27:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[SubCategories](
	[SubCategoryID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [nvarchar](50) NOT NULL,
	[ParentID] [int] NOT NULL,
 CONSTRAINT [SubCategories$PrimaryKey] PRIMARY KEY CLUSTERED 
(
	[SubCategoryID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Index [BillOfMaterials$ComponentID]    Script Date: 2021-10-12 14:27:34 ******/
CREATE NONCLUSTERED INDEX [BillOfMaterials$ComponentID] ON [dbo].[BillOfMaterialsItems]
(
	[ComponentID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [BillOfMaterials$PrimaryKey]    Script Date: 2021-10-12 14:27:34 ******/
CREATE UNIQUE NONCLUSTERED INDEX [BillOfMaterials$PrimaryKey] ON [dbo].[BillOfMaterialsItems]
(
	[ItemID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [BillOfMaterials$ProjectID]    Script Date: 2021-10-12 14:27:34 ******/
CREATE NONCLUSTERED INDEX [BillOfMaterials$ProjectID] ON [dbo].[BillOfMaterialsItems]
(
	[ProjectID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [Categories$ID]    Script Date: 2021-10-12 14:27:34 ******/
CREATE UNIQUE NONCLUSTERED INDEX [Categories$ID] ON [dbo].[Categories]
(
	[CategoryID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [Categories$Name]    Script Date: 2021-10-12 14:27:34 ******/
CREATE UNIQUE NONCLUSTERED INDEX [Categories$Name] ON [dbo].[Categories]
(
	[Name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [Components$Name]    Script Date: 2021-10-12 14:27:34 ******/
CREATE UNIQUE NONCLUSTERED INDEX [Components$Name] ON [dbo].[Components]
(
	[Name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [Components$PrimaryKey]    Script Date: 2021-10-12 14:27:34 ******/
CREATE UNIQUE NONCLUSTERED INDEX [Components$PrimaryKey] ON [dbo].[Components]
(
	[ComponentID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [IX_Datasheets]    Script Date: 2021-10-12 14:27:34 ******/
CREATE NONCLUSTERED INDEX [IX_Datasheets] ON [dbo].[Datasheets]
(
	[DatasheetID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [Packages$Name]    Script Date: 2021-10-12 14:27:34 ******/
CREATE UNIQUE NONCLUSTERED INDEX [Packages$Name] ON [dbo].[Packages]
(
	[Name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [Packages$PrimaryKey]    Script Date: 2021-10-12 14:27:34 ******/
CREATE UNIQUE NONCLUSTERED INDEX [Packages$PrimaryKey] ON [dbo].[Packages]
(
	[PackageID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [Projects$Name]    Script Date: 2021-10-12 14:27:34 ******/
CREATE NONCLUSTERED INDEX [Projects$Name] ON [dbo].[Projects]
(
	[Name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [Projects$PrimaryKey]    Script Date: 2021-10-12 14:27:34 ******/
CREATE UNIQUE NONCLUSTERED INDEX [Projects$PrimaryKey] ON [dbo].[Projects]
(
	[ProjectID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [Properties$ComponentID]    Script Date: 2021-10-12 14:27:34 ******/
CREATE NONCLUSTERED INDEX [Properties$ComponentID] ON [dbo].[Properties]
(
	[ComponentID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [SubCategories$ID]    Script Date: 2021-10-12 14:27:34 ******/
CREATE NONCLUSTERED INDEX [SubCategories$ID] ON [dbo].[SubCategories]
(
	[SubCategoryID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [SubCategories$Name]    Script Date: 2021-10-12 14:27:34 ******/
CREATE NONCLUSTERED INDEX [SubCategories$Name] ON [dbo].[SubCategories]
(
	[Name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
ALTER TABLE [dbo].[BillOfMaterialsItems] ADD  CONSTRAINT [DF__BillOfMat__Quant__403A8C7D]  DEFAULT ((0)) FOR [Quantity]
GO
ALTER TABLE [dbo].[BillOfMaterialsItems] ADD  CONSTRAINT [DF_BillOfMaterialsItems_Populate]  DEFAULT ((1)) FOR [Populate]
GO
ALTER TABLE [dbo].[BillOfMaterialsItems] ADD  CONSTRAINT [DF__BillOfMat__Compo__412EB0B6]  DEFAULT ((0)) FOR [ComponentID]
GO
ALTER TABLE [dbo].[BillOfMaterialsItems] ADD  CONSTRAINT [DF__BillOfMat__Proje__4222D4EF]  DEFAULT ((0)) FOR [ProjectID]
GO
ALTER TABLE [dbo].[Components] ADD  CONSTRAINT [DF__Component__Quant__34C8D9D1]  DEFAULT ((0)) FOR [Quantity]
GO
ALTER TABLE [dbo].[Components] ADD  CONSTRAINT [DF__Component__Categ__35BCFE0A]  DEFAULT ((0)) FOR [CategoryID]
GO
ALTER TABLE [dbo].[Components] ADD  CONSTRAINT [DF__Component__SubCa__36B12243]  DEFAULT ((0)) FOR [SubCategoryID]
GO
ALTER TABLE [dbo].[Components] ADD  CONSTRAINT [DF__Component__Packa__37A5467C]  DEFAULT ((0)) FOR [PackageID]
GO
ALTER TABLE [dbo].[Datasheets] ADD  CONSTRAINT [DF__Datasheet__rowgu__32AB8735]  DEFAULT (newid()) FOR [rowguid]
GO
ALTER TABLE [dbo].[Images] ADD  DEFAULT (newid()) FOR [rowguid]
GO
ALTER TABLE [dbo].[Images] ADD  CONSTRAINT [DF_Images_ComponentID]  DEFAULT (NULL) FOR [ComponentID]
GO
ALTER TABLE [dbo].[Images] ADD  CONSTRAINT [DF_Images_PackageID]  DEFAULT (NULL) FOR [PackageID]
GO
ALTER TABLE [dbo].[Properties] ADD  CONSTRAINT [DF__Propertie__Compo__3B75D760]  DEFAULT (NULL) FOR [ComponentID]
GO
ALTER TABLE [dbo].[SubCategories] ADD  CONSTRAINT [DF__SubCatego__Paren__29572725]  DEFAULT ((0)) FOR [ParentID]
GO
ALTER TABLE [dbo].[BillOfMaterialsItems]  WITH CHECK ADD  CONSTRAINT [FK_BOMItem_Component] FOREIGN KEY([ComponentID])
REFERENCES [dbo].[Components] ([ComponentID])
GO
ALTER TABLE [dbo].[BillOfMaterialsItems] CHECK CONSTRAINT [FK_BOMItem_Component]
GO
ALTER TABLE [dbo].[BillOfMaterialsItems]  WITH CHECK ADD  CONSTRAINT [FK_BOMItem_Project] FOREIGN KEY([ProjectID])
REFERENCES [dbo].[Projects] ([ProjectID])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[BillOfMaterialsItems] CHECK CONSTRAINT [FK_BOMItem_Project]
GO
ALTER TABLE [dbo].[Components]  WITH NOCHECK ADD  CONSTRAINT [FK_Component_Category] FOREIGN KEY([CategoryID])
REFERENCES [dbo].[Categories] ([CategoryID])
GO
ALTER TABLE [dbo].[Components] CHECK CONSTRAINT [FK_Component_Category]
GO
ALTER TABLE [dbo].[Components]  WITH NOCHECK ADD  CONSTRAINT [FK_Component_Package] FOREIGN KEY([PackageID])
REFERENCES [dbo].[Packages] ([PackageID])
GO
ALTER TABLE [dbo].[Components] CHECK CONSTRAINT [FK_Component_Package]
GO
ALTER TABLE [dbo].[Components]  WITH NOCHECK ADD  CONSTRAINT [FK_Component_SubCategory] FOREIGN KEY([SubCategoryID])
REFERENCES [dbo].[SubCategories] ([SubCategoryID])
GO
ALTER TABLE [dbo].[Components] CHECK CONSTRAINT [FK_Component_SubCategory]
GO
ALTER TABLE [dbo].[Datasheets]  WITH CHECK ADD  CONSTRAINT [FK_Datasheet_Component] FOREIGN KEY([ComponentID])
REFERENCES [dbo].[Components] ([ComponentID])
GO
ALTER TABLE [dbo].[Datasheets] CHECK CONSTRAINT [FK_Datasheet_Component]
GO
ALTER TABLE [dbo].[Images]  WITH CHECK ADD  CONSTRAINT [FK_Image_Component] FOREIGN KEY([ComponentID])
REFERENCES [dbo].[Components] ([ComponentID])
GO
ALTER TABLE [dbo].[Images] CHECK CONSTRAINT [FK_Image_Component]
GO
ALTER TABLE [dbo].[Images]  WITH CHECK ADD  CONSTRAINT [FK_Image_Package] FOREIGN KEY([PackageID])
REFERENCES [dbo].[Packages] ([PackageID])
GO
ALTER TABLE [dbo].[Images] CHECK CONSTRAINT [FK_Image_Package]
GO
ALTER TABLE [dbo].[Properties]  WITH NOCHECK ADD  CONSTRAINT [FK_Property_Component] FOREIGN KEY([ComponentID])
REFERENCES [dbo].[Components] ([ComponentID])
GO
ALTER TABLE [dbo].[Properties] CHECK CONSTRAINT [FK_Property_Component]
GO
ALTER TABLE [dbo].[SubCategories]  WITH NOCHECK ADD  CONSTRAINT [FK_SubCategory_Category] FOREIGN KEY([ParentID])
REFERENCES [dbo].[Categories] ([CategoryID])
GO
ALTER TABLE [dbo].[SubCategories] CHECK CONSTRAINT [FK_SubCategory_Category]
GO
ALTER TABLE [dbo].[Categories]  WITH NOCHECK ADD  CONSTRAINT [SSMA_CC$Categories$Name$disallow_zero_length] CHECK  ((len([Name])>(0)))
GO
ALTER TABLE [dbo].[Categories] CHECK CONSTRAINT [SSMA_CC$Categories$Name$disallow_zero_length]
GO
ALTER TABLE [dbo].[Components]  WITH NOCHECK ADD  CONSTRAINT [SSMA_CC$Components$Name$disallow_zero_length] CHECK  ((len([Name])>(0)))
GO
ALTER TABLE [dbo].[Components] CHECK CONSTRAINT [SSMA_CC$Components$Name$disallow_zero_length]
GO
ALTER TABLE [dbo].[Packages]  WITH NOCHECK ADD  CONSTRAINT [SSMA_CC$Packages$Name$disallow_zero_length] CHECK  ((len([Name])>(0)))
GO
ALTER TABLE [dbo].[Packages] CHECK CONSTRAINT [SSMA_CC$Packages$Name$disallow_zero_length]
GO
ALTER TABLE [dbo].[SubCategories]  WITH NOCHECK ADD  CONSTRAINT [SSMA_CC$SubCategories$Name$disallow_zero_length] CHECK  ((len([Name])>(0)))
GO
ALTER TABLE [dbo].[SubCategories] CHECK CONSTRAINT [SSMA_CC$SubCategories$Name$disallow_zero_length]
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Bill of Materials component ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BillOfMaterialsItems', @level2type=N'COLUMN',@level2name=N'ItemID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[BillOfMaterials].[ID]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BillOfMaterialsItems', @level2type=N'COLUMN',@level2name=N'ItemID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Quantity of this component' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BillOfMaterialsItems', @level2type=N'COLUMN',@level2name=N'Quantity'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[BillOfMaterials].[Quantity]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BillOfMaterialsItems', @level2type=N'COLUMN',@level2name=N'Quantity'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Reference designators separated by a comma and space' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BillOfMaterialsItems', @level2type=N'COLUMN',@level2name=N'RefDes'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[BillOfMaterials].[RefDes]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BillOfMaterialsItems', @level2type=N'COLUMN',@level2name=N'RefDes'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Should we populate this component?' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BillOfMaterialsItems', @level2type=N'COLUMN',@level2name=N'Populate'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID of the component in the Components database' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BillOfMaterialsItems', @level2type=N'COLUMN',@level2name=N'ComponentID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[BillOfMaterials].[ComponentID]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BillOfMaterialsItems', @level2type=N'COLUMN',@level2name=N'ComponentID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Project that this Bill of Materials is a parent of' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BillOfMaterialsItems', @level2type=N'COLUMN',@level2name=N'ProjectID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[BillOfMaterials].[ProjectID]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BillOfMaterialsItems', @level2type=N'COLUMN',@level2name=N'ProjectID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[BillOfMaterials].[ID]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BillOfMaterialsItems', @level2type=N'CONSTRAINT',@level2name=N'BillOfMaterials$ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[BillOfMaterials].[ComponentID]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BillOfMaterialsItems', @level2type=N'INDEX',@level2name=N'BillOfMaterials$ComponentID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[BillOfMaterials].[PrimaryKey]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BillOfMaterialsItems', @level2type=N'INDEX',@level2name=N'BillOfMaterials$PrimaryKey'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[BillOfMaterials].[ProjectID]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BillOfMaterialsItems', @level2type=N'INDEX',@level2name=N'BillOfMaterials$ProjectID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[BillOfMaterials]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BillOfMaterialsItems'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[BillOfMaterials].[ComponentsBillOfMaterials]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BillOfMaterialsItems', @level2type=N'CONSTRAINT',@level2name=N'FK_BOMItem_Component'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[BillOfMaterials].[ProjectsBillOfMaterials]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'BillOfMaterialsItems', @level2type=N'CONSTRAINT',@level2name=N'FK_BOMItem_Project'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Category ID number' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Categories', @level2type=N'COLUMN',@level2name=N'CategoryID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Categories].[ID]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Categories', @level2type=N'COLUMN',@level2name=N'CategoryID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Name of the category' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Categories', @level2type=N'COLUMN',@level2name=N'Name'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Categories].[Name]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Categories', @level2type=N'COLUMN',@level2name=N'Name'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Categories].[PrimaryKey]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Categories', @level2type=N'CONSTRAINT',@level2name=N'Categories$PrimaryKey'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Categories].[ID]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Categories', @level2type=N'INDEX',@level2name=N'Categories$ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Categories].[Name]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Categories', @level2type=N'INDEX',@level2name=N'Categories$Name'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Categories]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Categories'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Row identification' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Components', @level2type=N'COLUMN',@level2name=N'ComponentID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Components].[ID]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Components', @level2type=N'COLUMN',@level2name=N'ComponentID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Manufacturer Part Number' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Components', @level2type=N'COLUMN',@level2name=N'Name'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Components].[Name]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Components', @level2type=N'COLUMN',@level2name=N'Name'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Number of components in stock' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Components', @level2type=N'COLUMN',@level2name=N'Quantity'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Components].[Quantity]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Components', @level2type=N'COLUMN',@level2name=N'Quantity'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'A brief description of the component' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Components', @level2type=N'COLUMN',@level2name=N'Description'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Components].[Notes]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Components', @level2type=N'COLUMN',@level2name=N'Description'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Component category' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Components', @level2type=N'COLUMN',@level2name=N'CategoryID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Components].[CategoryID]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Components', @level2type=N'COLUMN',@level2name=N'CategoryID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Component sub-category' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Components', @level2type=N'COLUMN',@level2name=N'SubCategoryID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Components].[SubCategoryID]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Components', @level2type=N'COLUMN',@level2name=N'SubCategoryID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Component package' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Components', @level2type=N'COLUMN',@level2name=N'PackageID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Components].[PackageID]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Components', @level2type=N'COLUMN',@level2name=N'PackageID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Components].[ID]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Components', @level2type=N'CONSTRAINT',@level2name=N'Components$ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Components].[Name]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Components', @level2type=N'INDEX',@level2name=N'Components$Name'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Components].[PrimaryKey]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Components', @level2type=N'INDEX',@level2name=N'Components$PrimaryKey'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Components]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Components'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Components].[CategoriesComponents]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Components', @level2type=N'CONSTRAINT',@level2name=N'FK_Component_Category'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Components].[PackagesComponents]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Components', @level2type=N'CONSTRAINT',@level2name=N'FK_Component_Package'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Components].[SubCategoriesComponents]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Components', @level2type=N'CONSTRAINT',@level2name=N'FK_Component_SubCategory'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Package ID number' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Packages', @level2type=N'COLUMN',@level2name=N'PackageID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Packages].[ID]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Packages', @level2type=N'COLUMN',@level2name=N'PackageID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Package name' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Packages', @level2type=N'COLUMN',@level2name=N'Name'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Packages].[Name]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Packages', @level2type=N'COLUMN',@level2name=N'Name'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Packages].[ID]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Packages', @level2type=N'CONSTRAINT',@level2name=N'Packages$ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Packages].[Name]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Packages', @level2type=N'INDEX',@level2name=N'Packages$Name'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Packages].[PrimaryKey]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Packages', @level2type=N'INDEX',@level2name=N'Packages$PrimaryKey'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Packages]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Packages'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Project identification' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Projects', @level2type=N'COLUMN',@level2name=N'ProjectID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Projects].[ID]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Projects', @level2type=N'COLUMN',@level2name=N'ProjectID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Name of the project' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Projects', @level2type=N'COLUMN',@level2name=N'Name'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Projects].[Name]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Projects', @level2type=N'COLUMN',@level2name=N'Name'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Revision of the project' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Projects', @level2type=N'COLUMN',@level2name=N'Revision'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Projects].[Revision]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Projects', @level2type=N'COLUMN',@level2name=N'Revision'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Description of the project' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Projects', @level2type=N'COLUMN',@level2name=N'Description'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Projects].[Description]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Projects', @level2type=N'COLUMN',@level2name=N'Description'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Projects].[ID]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Projects', @level2type=N'CONSTRAINT',@level2name=N'Projects$ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Projects].[Name]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Projects', @level2type=N'INDEX',@level2name=N'Projects$Name'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Projects].[PrimaryKey]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Projects', @level2type=N'INDEX',@level2name=N'Projects$PrimaryKey'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Projects]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Projects'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Row identification' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Properties', @level2type=N'COLUMN',@level2name=N'PropertyID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Properties].[ID]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Properties', @level2type=N'COLUMN',@level2name=N'PropertyID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Name of the property' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Properties', @level2type=N'COLUMN',@level2name=N'Name'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Properties].[Name]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Properties', @level2type=N'COLUMN',@level2name=N'Name'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Value of the property' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Properties', @level2type=N'COLUMN',@level2name=N'Value'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Properties].[Value]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Properties', @level2type=N'COLUMN',@level2name=N'Value'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID of the component associated with this property' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Properties', @level2type=N'COLUMN',@level2name=N'ComponentID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Properties].[ComponentID]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Properties', @level2type=N'COLUMN',@level2name=N'ComponentID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Properties].[ID]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Properties', @level2type=N'CONSTRAINT',@level2name=N'Properties$ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Properties].[ComponentID]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Properties', @level2type=N'INDEX',@level2name=N'Properties$ComponentID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Properties]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Properties'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[Properties].[ComponentsProperties]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Properties', @level2type=N'CONSTRAINT',@level2name=N'FK_Property_Component'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Sub-category ID number' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SubCategories', @level2type=N'COLUMN',@level2name=N'SubCategoryID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[SubCategories].[ID]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SubCategories', @level2type=N'COLUMN',@level2name=N'SubCategoryID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Sub-category name' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SubCategories', @level2type=N'COLUMN',@level2name=N'Name'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[SubCategories].[Name]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SubCategories', @level2type=N'COLUMN',@level2name=N'Name'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Parent category' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SubCategories', @level2type=N'COLUMN',@level2name=N'ParentID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[SubCategories].[ParentID]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SubCategories', @level2type=N'COLUMN',@level2name=N'ParentID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[SubCategories].[PrimaryKey]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SubCategories', @level2type=N'CONSTRAINT',@level2name=N'SubCategories$PrimaryKey'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[SubCategories].[ID]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SubCategories', @level2type=N'INDEX',@level2name=N'SubCategories$ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[SubCategories].[Name]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SubCategories', @level2type=N'INDEX',@level2name=N'SubCategories$Name'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[SubCategories]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SubCategories'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_SSMA_SOURCE', @value=N'PartCat.[SubCategories].[CategoriesSubCategories]' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'SubCategories', @level2type=N'CONSTRAINT',@level2name=N'FK_SubCategory_Category'
GO
USE [master]
GO
ALTER DATABASE [PartsCatalog] SET  READ_WRITE 
GO
