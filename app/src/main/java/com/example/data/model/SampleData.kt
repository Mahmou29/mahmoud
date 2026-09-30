package com.example.data.model

object SampleData {
    val demoPrompts = listOf(
        QuickDemoPrompt(
            id = "demo_1",
            label = "افتتاح كافيه في التجمع (Design / Video)",
            recommendedTeam = TargetTeam.ALL,
            rawText = """يا شباب بنفتح كافيه ومخبوزات جديد في التجمع الأسبوع الجاي.
عايزين حاجة شيك وClean جداً تليق بالناس اللي هناك، مش عايزين دوشة وألوان فاقعة، خلو الـ Vibe هادي وPremium بس في نفس الوقت مش أوفر ولا رسمي زيادة.
محتاجين كذا بوست انستجرام مع Reel حلو يشد الناس للافتتاح، ولسه مستنيين المنيو النهائي يخلص، بس أهم حاجة الناس تعرف إن القهوة والمخبوزات طازة بتتعمل قدامهم."""
        ),
        QuickDemoPrompt(
            id = "demo_2",
            label = "منتج سكين كير للبنات (Video / Content)",
            recommendedTeam = TargetTeam.VIDEO,
            rawText = """عايزين Reel لتيك توك وإنستجرام لسيروم طبيعي للشعر معمول من زيوت طبيعية.
عايزين فكرة مختلفة مش تقليدية زي فيديوهات الإنفلونسرز المكررة، حاجة تبان Real وفيها مصداقية، ومحتاجين نركز على فكرة إنه خفيف ومش بيلزق وسعره اقتصادي مقارنة بالبراندات المستوردة الغالية."""
        ),
        QuickDemoPrompt(
            id = "demo_3",
            label = "تطبيق محاسبة للشركات (Account / Design)",
            recommendedTeam = TargetTeam.ACCOUNT,
            rawText = """العميل باعت بيقول:
عايزين حملة سريعة لسوفت وير فواتير ومحاسبة للشركات الصغيرة، الهدف نوصل لـ 500 تسجيل تجريبي مجاني.
بيقول مش عايز كلام بنوك معقد، عايز الإعلان يوصل للمدير أو صاحب البيزنس إنه هيرتاح من وجع دماغ الحسابات في ثانية. الميزانية محدودة وعايزين يبدأوا الإثنين الجاي وماعندهمش أي صور أو فيديوهات جاهزة."""
        ),
        QuickDemoPrompt(
            id = "demo_4",
            label = "Smart Water Bottle Launch (English / All)",
            recommendedTeam = TargetTeam.ALL,
            rawText = """We're launching our new smart vacuum water bottle next month.
Need something super slick, high-end Apple-like minimal vibe. Target audience is young professionals & fitness enthusiasts in Cairo and Dubai.
We need 3 carousel posts and a high-tempo launch Reel.
Don't make it look cheesy or colorful, keep it sleek dark mode with subtle neon water droplets.
We don't have the final retail price yet, but need the creative directions by Thursday."""
        )
    )
}
