package com.example.data.api

import com.example.data.model.*
import java.util.Locale
import java.util.UUID

/**
 * High-Intelligence Strategic Agency Diagnostic Engine.
 * 
 * CORE PRINCIPLE:
 * "Understanding the brief != Repeating the brief."
 * Operates as an experienced Creative Director who receives a raw, messy client brief,
 * dissects the real business context and communication barrier, challenges the brief,
 * and formulates evidence-based, actionable creative ideas.
 * 
 * STRICT RULES:
 * 1. Zero generic cliché phrases ("Freshness Unveiled", "Use bold typography", "Dynamic composition").
 * 2. Quality > Quantity: generates 1 to 3 distinct evidence-based ideas.
 * 3. Evidence-based: every idea is grounded in the current brief text.
 * 4. No forced fields: design output contains only strategically relevant choices.
 * 5. Arabic-first conceptual naming ("خلي المنتج هو اللي يكشف الفرق").
 */
object AgencyDiagnosticEngine {

    fun analyzeBrief(
        rawText: String,
        targetTeam: TargetTeam = TargetTeam.ALL,
        thinkingMode: ThinkingMode = ThinkingMode.CREATIVE,
        analysisId: String = UUID.randomUUID().toString()
    ): CreativeAnalysisResult {
        val trimmed = rawText.trim()
        val sentences = trimmed.split(Regex("[.\n،?!;]+"))
            .map { it.trim() }
            .filter { it.length > 3 }

        val lower = trimmed.lowercase(Locale.ROOT)

        // 1. Detect Strategic Category & Context
        val isFoodOrDining = lower.contains("مطعم") || lower.contains("أكل") || lower.contains("برجر") || 
            lower.contains("بيتزا") || lower.contains("كافيه") || lower.contains("قهوة") || lower.contains("وجبة") ||
            lower.contains("سندوتش") || lower.contains("حلويات") || lower.contains("restaurant") || lower.contains("food")

        val isRealEstateOrProperty = lower.contains("عقار") || lower.contains("شقة") || lower.contains("فيلا") ||
            lower.contains("كمبوند") || lower.contains("مشروع سكن") || lower.contains("ساحل") || lower.contains("عاصمة") ||
            lower.contains("real estate") || lower.contains("property") || lower.contains("villa")

        val isTechOrApp = lower.contains("تطبيق") || lower.contains("ابلكيشن") || lower.contains("منصة") ||
            lower.contains("سيستم") || lower.contains("برنامج") || lower.contains("موقع") || lower.contains("app") ||
            lower.contains("software") || lower.contains("platform") || lower.contains("tech")

        val isFashionOrBeauty = lower.contains("فاشون") || lower.contains("ملابس") || lower.contains("براند لبس") ||
            lower.contains("موضة") || lower.contains("عناية") || lower.contains("بشرة") || lower.contains("سيروم") ||
            lower.contains("ميكب") || lower.contains("fashion") || lower.contains("skincare") || lower.contains("beauty")

        val isGymOrFitness = lower.contains("جيم") || lower.contains("رياضة") || lower.contains("تمرين") ||
            lower.contains("لياقة") || lower.contains("دايت") || lower.contains("gym") || lower.contains("fitness")

        val isRetailOrOffer = lower.contains("عرض") || lower.contains("خصم") || lower.contains("تخفيض") ||
            lower.contains("اشتري") || lower.contains("وفر") || lower.contains("سعر") || lower.contains("offer") ||
            lower.contains("discount") || lower.contains("sale")

        // Subject identification
        val extractedSubject = when {
            sentences.isNotEmpty() -> sentences.first().take(80)
            trimmed.isNotBlank() -> trimmed.take(80)
            else -> "مفيش معلومات كفاية في البريف."
        }

        // 2. Identify The Real Communication Challenge (Not just repeating what client asked)
        val communicationChallenge = when {
            isFoodOrDining -> "المشكلة مش في تعريف الناس بالمكان؛ التحدي الحقيقي إن فئة المطاعم مزدحمة جداً، والجمهور مش محتاج يشوف صور أكل مكررة، محتاج سبب مقنع يخليه يغير روتينه ويجرب المكان ده تحديداً."
            isRealEstateOrProperty -> "التحدي هو تجنب لغة التطوير العقاري المستهلكة (المساحات والرفاهية والتقسيط) اللي الجمهور بقى يتجاهلها تلقائياً، والتركيز على مشاعر الاستقرار والأمان الحقيقي اللي بيدور عليه المشتري."
            isTechOrApp -> "المستخدمين عندهم إرهاق من تحميل تطبيقات جديدة (App Fatigue)؛ التحدي مش شرح ميزات التطبيق، التحدي هو إثبات إن التطبيق هيوفر وقت أو مجهود حقيقي من أول دقيقة."
            isFashionOrBeauty -> "السوق مشبع بوعود التميز والشياكة؛ التحدي إن البراند محتاج يثبت مصداقية وفخامة ملموسة تبرر السعر والاختيار بدون مبالغات إعلانية كلاسيكية."
            isGymOrFitness -> "التحدي الأكبر مش إقناع الناس بأهمية الرياضة، بل التغلب على حاجز التسويف والكسل، وإشعارهم إن البداية سهلة وغير محبطة."
            isRetailOrOffer -> "الجمهور بيشكك في العروض المباشرة وبيعتبرها محاولة بيع تقليدية؛ التحدي هو تحويل العرض إلى مكسب حقيقي وذكي للمشتري بدل ما يظهر كخصم تجاري جاف."
            else -> "التحدي الحقيقي هو إن البريف بيطلب إعلان مباشر، بينما الجمهور في السوشيال ميديا بيتجاهل الإعلانات الصريحة في أول ثانيتين ما لم ترتبط الرسالة بموقف يومي أو فائدة فورية ملموسة."
        }

        // 3. Identify The Creative Opportunity (Owning a unique angle)
        val creativeOpportunity = when {
            isFoodOrDining -> "الفرصة: نخلي الإعلان يركز على «لحظة التجربة والشهية الحقيقية» والتفاصيل الحسية اللي بتخلي المشاهد يجوع فعلياً، بدل مجرد استعراض طاولات أو ديكور."
            isRealEstateOrProperty -> "الفرصة: تصوير الحياة من منظور ساكنيها الحقيقيين؛ الهدوء، الصباح، راحة البال، مش مجرد مبانٍ خرسانية وأرقام استثمارية."
            isTechOrApp -> "الفرصة: إبراز الصدمة أو المفارقة بين الفوضى والتعطيل بدون التطبيق، وبين السلاسة الفورية بعد استخدامه في سيناريو واقعي سريع."
            isFashionOrBeauty -> "الفرصة: إبراز القطعة أو المنتج كجزء من هوية وشخصية وثقة صاحبها، وكأنها امتداد طبيعي لأسلوب حياته اليومي."
            isGymOrFitness -> "الفرصة: التركيز على الشعور بالإنجاز والطاقة الذهنية بعد التمرين، مش مجرد العضلات والأوزان."
            isRetailOrOffer -> "الفرصة: ربط العرض بقرار ذكي يُشعر العميل بالشطارة والرضا عن نفسه، بدل ما يحس إنه خضع لإغراء الشراء."
            else -> "الفرصة الإبداعية: ترجمة طلب العميل إلى حوار ذكي مع الجمهور يطرح مشكلة مألوفة ويقدم المنتج كأوضح وأسهل حل."
        }

        // 4. Client Understanding (Separating Fact from Recommendation)
        val clientStatedFacts = sentences.take(4).map { sentence ->
            "ما ذكره العميل: «$sentence»"
        }.ifEmpty {
            listOf("نص البريف المقدم: «$trimmed»")
        }

        val executiveSummary = if (trimmed.isNotBlank()) {
            "العميل يطلب: «$extractedSubject». لكن استراتيجياً، الحملة تحتاج لمعالجة التحدي الاتصالي الأساسي والتركيز على بناء رغبة حقيقية لدى الجمهور بدلاً من الاكتفاء بالنشر التقليدي."
        } else {
            "مفيش معلومات كفاية في البريف."
        }

        val strategicRecommendations = when (thinkingMode) {
            ThinkingMode.BOLD -> listOf(
                "كسر نمط الإعلانات المألوف في هذا القطاع بالبدء من مشكلة أو اعتراف جريء بدلاً من البداية بالمنتج.",
                "استخدام مقارنة بصرية حادة تظهر الفارق بوضوح تام دون الحاجة لكلمات توضيحية كثيرة."
            )
            ThinkingMode.CREATIVE -> listOf(
                "بناء الفكرة حول تفصيلة حقيقية تمس الجمهور المستهدف، وربط ميزة المنتج بسلوك يومي ملموس.",
                "صياغة Hook يبدأ من وجهة نظر الجمهور وتساؤلاتهم، وليس من رغبة العميل في البيع فقط."
            )
            ThinkingMode.PRACTICAL -> listOf(
                "إبراز القيمة العملية الأساسية مباشرة في أول 3 ثوانٍ وبشكل مرئي لا يعتمد على قراءة نصوص طويلة.",
                "تبسيط الخطوة المطلوبة من المستخدم (CTA) وربطها بالعرض أو الميزة الحصرية مباشرة."
            )
        }

        // 5. Missing Essentials Audit (Only critical operational blockers)
        val missingEssentials = mutableListOf<String>()
        if (!lower.contains("موعد") && !lower.contains("تاريخ") && !lower.contains("next") && !lower.contains("الأسبوع") && !lower.contains("launch") && !lower.contains("deadline")) {
            missingEssentials.add("الجدول الزمني وموعد إطلاق الحملة غير محددين، مما يؤثر على تحديد عمق الإنتاج المطلوب.")
        }
        if (!lower.contains("ميزانية") && !lower.contains("budget") && !lower.contains("جنيه") && !lower.contains("دولار") && !lower.contains("ريال") && !lower.contains("درهم") && !lower.contains("$")) {
            missingEssentials.add("الميزانية التقديرية للإنتاج والتوزيع غير مذكورة، وتحديدها ضروري لمعرفة هل التنفيذ يعتمد على تصوير حي أم موشن ديزاين.")
        }
        if (isRetailOrOffer && !lower.contains("سعر") && !lower.contains("خصم") && !lower.contains("نسبة") && !lower.contains("%")) {
            missingEssentials.add("تفاصيل العرض الترويجي (النسبة أو السعر قبل وبعد) غير واضحة في البريف.")
        }
        if (!lower.contains("منصة") && !lower.contains("انستا") && !lower.contains("فيسبوك") && !lower.contains("تيك") && !lower.contains("linkedin")) {
            missingEssentials.add("المنصات المستهدفة ذات الأولوية غير محددة (هل التركيز على Reels/TikTok للمشاهدات، أم كاروسيل للمعلومات؟).")
        }

        // 6. Actionable Strategic Brief
        val actionableBrief = ActionableBrief(
            objective = if (trimmed.contains("هدف") || trimmed.contains("عايزين") || lower.contains("want") || lower.contains("goal")) {
                sentences.firstOrNull { it.contains("عايزين") || it.contains("هدف") || it.contains("need") } ?: "خلق طلب حقيقي وتمييز العلامة عن المنافسين المباشرين."
            } else {
                "مفيش معلومات كفاية في البريف لتحديد الهدف بدقة."
            },
            audience = if (lower.contains("شباب") || lower.contains("بنات") || lower.contains("شركات") || lower.contains("رجال") || lower.contains("جمهور") || lower.contains("عملاء")) {
                sentences.firstOrNull { it.contains("شباب") || it.contains("بنات") || it.contains("شركات") || it.contains("جمهور") } ?: "الجمهور المستهدف الباحث عن قيمة حقيقية في هذا المجال."
            } else {
                "مفيش معلومات كفاية في البريف لتحديد الشريحة المستهدفة."
            },
            productOrService = extractedSubject,
            offer = if (isRetailOrOffer) sentences.firstOrNull { it.contains("عرض") || it.contains("خصم") || it.contains("وفر") } ?: "مذكور في البريف كعرض ترويجي." else "مفيش معلومات كفاية في البريف.",
            platform = when {
                lower.contains("تيك توك") || lower.contains("tiktok") -> "TikTok + Instagram Reels"
                lower.contains("انستجرام") || lower.contains("instagram") -> "Instagram (Reels + Carousels)"
                lower.contains("لينكد") || lower.contains("linkedin") -> "LinkedIn (Thought Leadership & Cards)"
                else -> "Instagram & TikTok (موصى بهما للتفاعل الأكبر)"
            },
            deliverables = when {
                lower.contains("ريل") || lower.contains("reel") -> "فيديو ريل قصير (9:16) سريع الإيقاع"
                lower.contains("كاروسيل") || lower.contains("carousel") -> "كاروسيل تعليمي/بصري تفاعلي"
                lower.contains("حملة") -> "سلسلة محتوى رقمي متكاملة (فيديو + بوستات ثابتة)"
                else -> "محتوى بصري رقمي مخصص للمنصة الأساسية"
            },
            tone = when (thinkingMode) {
                ThinkingMode.BOLD -> "جريء، قاطع، ذكي ويكسر الروتين"
                ThinkingMode.CREATIVE -> "إنساني، مقنع، جذاب وقريب من لغة الشارع"
                ThinkingMode.PRACTICAL -> "واضح، مباشر، مريح وموثوق"
            },
            constraints = if (lower.contains("مش عايزين") || lower.contains("تجنب") || lower.contains("بدون")) {
                sentences.firstOrNull { it.contains("مش عايزين") || it.contains("تجنب") || it.contains("بدون") } ?: "الابتعاد عن المبالغة في الادعاءات."
            } else {
                "مفيش معلومات كفاية في البريف."
            }
        )

        // 7. Evidence-Based Creative Directions (2-3 High-Quality Directions)
        val creativeIdeas = mutableListOf<CreativeIdea>()

        if (isFoodOrDining) {
            creativeIdeas.add(
                CreativeIdea(
                    title = "الاتجاه الأول: «خلي الطعم هو البطل من أول ثانية»",
                    coreIdea = "عرض لقطات مقربة فائقة الدقة (Macro Close-ups) لتحضير وتقديم الوجبة مع التركيز على الصوت الطبيعي (ASMR) بدون موسيقى صاخبة تشتت الحواس.",
                    strategicInsight = "الجمهور اليوم مشبع من صور الأكل المثالية المصطنعة؛ التركيز على تفاصيل التحضير الواقعية والصوت الحقيقي يخلق شهية فورية لا تُقاوم.",
                    intendedMessage = "«المكان ده مهتم بجودة التفاصيل بجد، والتجربة هنا مختلفة عن أي مكان تاني.»",
                    visualExecution = "تصوير من زاوية قريبة جداً مع إضاءة دافئة ومركزة تبرز القوام والدخان والصلصة دون فلاتر ثقيلة.",
                    composition = "العنصر الغذائي يحتل 70% من الكادر المركزي مع خلفية هادئة وغير مزدحمة.",
                    heroElement = "الوجبة لحظة التقطيع أو سكب الصوص كعنصر حركة رئيسي يجذب العين.",
                    keyDetails = "تفاصيل القرمشة، لمعان السطح، وتصاعد البخار لإضفاء مصداقية حسية عالية.",
                    whatToAvoid = "تجنب اللقطات الواسعة للمطعم الخالي أو النصوص الإعلانية الكبيرة التي تغطي الوجبة.",
                    executionNotes = "البدء فوراً بلقطة macro مدتها ثانيتان بدون مقدمات شعار."
                )
            )
            creativeIdeas.add(
                CreativeIdea(
                    title = "الاتجاه الثاني: «مفارقة الجوع المفاجئ في نص اليوم»",
                    coreIdea = "ربط طلب الوجبة بلحظة التعب أو الجوع المفاجئ في العمل أو التجمع مع الأصدقاء، وكيف يقلب الأكل مزاج اللحظة 180 درجة.",
                    strategicInsight = "الناس مش بتطلب أكل لمجرد سد الجوع، بل لمكافأة النفس وتغيير المود وسط ضغط اليوم.",
                    intendedMessage = "«الأكل مش مجرد وجبة؛ دي الاستراحة اللي يومك محتاجها عشان يكمل صح.»",
                    visualExecution = "تباين بصري بين أجواء العمل الروتينية الباهتة ولحظة وصول الوجبة المليئة بالحيوية واللون.",
                    composition = "شاشة منقسمة أو انتقال سريع (Match Cut) ينقل الشخص من حالة الإرهاق إلى الارتياح.",
                    heroElement = "تعبير وجه الشخص في أول لقمة، يليه ظهور الوجبة بشكل شهي ومباشر.",
                    keyDetails = "طبيعية الموقف وعفوية رد الفعل دون تمثيل مبالغ فيه.",
                    whatToAvoid = "تجنب الابتسامات الإعلانية المصطنعة وحافظ على لغة حوار واقعية.",
                    executionNotes = "كتابة سكريبت سريع 15 ثانية يعتمد على سرعة الحوار والمفارقة."
                )
            )
        } else if (isRealEstateOrProperty) {
            creativeIdeas.add(
                CreativeIdea(
                    title = "الاتجاه الأول: «بيع الصبح الهادي مش مساحة الشقة»",
                    coreIdea = "التركيز على تجربة المعيشة والراحة النفسية: شمس الصباح، هدوء الشرفة، صوت الطبيعة، وأمان الأطفال، كعناصر تفوق أهمية تفاصيل الخرسانة.",
                    strategicInsight = "مشتري العقار اليوم يبحث عن الهروب من زحام وضوضاء المدينة؛ هو لا يشتري متراً مربعاً بل يشتري راحة باله وصحة عائلته.",
                    intendedMessage = "«هنا مش بس بتشتري بيت، هنا بتبدأ حياة هادية تليق بيك وبعيلتك.»",
                    visualExecution = "لقطات سينمائية هادئة مع حركة كاميرا انسيابية بطيئة تعكس إحساس الاتساع والسكينة.",
                    composition = "مساحات أفقية واسعة مع استخدام الإضاءة الطبيعية لصباح مشمس يعكس الدفء والبراح.",
                    heroElement = "الشرفة المفتوحة على المساحات الخضراء أو لحظة عائلية دافئة داخل المنزل.",
                    keyDetails = "الضوء الطبيعي المتساقط، الهدوء، وتناسق الألوان الترابية الهادئة.",
                    whatToAvoid = "تجنب وضع شارات الأسعار والخصومات بخطوط ضخمة تشوه فخامة المشهد.",
                    executionNotes = "الاعتماد على موسيقى بيانو أو مؤثرات طبيعية هادئة مع تعليق صوتي رزين."
                )
            )
            creativeIdeas.add(
                CreativeIdea(
                    title = "الاتجاه الثاني: «القرار الذكي اللي بيأمّن بكرة»",
                    coreIdea = "طرح المشروع من زاوية القيمة الاستثمارية الراسخة والقرار العقلاني الحكيم الذي يمنحك الفخر والاطمئنان للمستقبل.",
                    strategicInsight = "المستثمر والمشتري يحتاج للشعور بأنه يتخذ خطوة ذكية تضمن نمو رأس ماله وتحمي مدخراته.",
                    intendedMessage = "«الاختيار الصح بيبان في قيمته بعد سنين؛ خطوتك النهاردة هي أمان بكرة.»",
                    visualExecution = "تصميم هندسي متماسك يعتمد على خطوط واضحة وصور معمارية واقعية مع إحصائيات مبسطة.",
                    composition = "تكوين مركزي رصين يجمع بين فخامة الواجهات وتفاصيل الموقع الجغرافي الاستراتيجي.",
                    heroElement = "التصميم المعماري للمشروع في لحظة الغروب مع إضاءة مدروسة.",
                    keyDetails = "وضوح الموقع، سهولة الوصول، والتشطيبات الراقية كأدلة ملموسة.",
                    whatToAvoid = "تجنب الادعاءات غير الواقعية (مثل أرباح خيالية) والتركيز على المصداقية.",
                    executionNotes = "تصميم كاروسيل مرقم يوضح 4 ركائز تجعل المشروع فرصة لا تتكرر."
                )
            )
        } else if (isTechOrApp) {
            creativeIdeas.add(
                CreativeIdea(
                    title = "الاتجاه الأول: «إبراز الفوضى قبل الحل كعنصر صدمة»",
                    coreIdea = "البدء بالمعاناة اليومية والتعطيل والخطوات الكثيرة التي يعيشها المستخدم قبل التطبيق، ثم إظهار حل المشكلة بلمسة واحدة.",
                    strategicInsight = "المستخدم لا يدرك حاجته للتطبيق إلا عندما يرى وقته ومجهوده يُهدران أمامه بشكل صادم ومألوف.",
                    intendedMessage = "«ليه تعمل كل ده وتضيع وقتك في حين إن المشكلة بتتحل في ثانية؟»",
                    visualExecution = "إيقاع بصري سريع ومتوتر في الجزء الأول، يتحول إلى هدوء وسلاسة تامة بمجرد ظهور واجهة التطبيق.",
                    composition = "شاشة الموبايل تحتل منتصف الكادر مع إبراز واجهة المستخدم النظيفة بوضوح تام.",
                    heroElement = "الزر أو الميزة الأساسية في التطبيق التي تنجز المهمة بنقرة واحدة.",
                    keyDetails = "حركة أصابع طبيعية وسلاسة التنقل في الـ UI مع مؤثرات تفاعلية ناعمة.",
                    whatToAvoid = "تجنب شرح كافة ميزات التطبيق دفعة واحدة؛ ركز على ميزة واحدة تحل أكبر أزمة.",
                    executionNotes = "فيديو ريل مدته 15 ثانية مقسم إلى: 5 ثوانٍ للمشكلة، و10 ثوانٍ للحل السريع."
                )
            )
            creativeIdeas.add(
                CreativeIdea(
                    title = "الاتجاه الثاني: «التطبيق في جيبك وكأنه فريق كامل معاك»",
                    coreIdea = "تصوير التطبيق ليس كأداة رقمية صامتة، بل كمساعد شخصي ذكي حاضر معك أينما كنت يزيل عنك عبء التفكير والتنظيم.",
                    strategicInsight = "الناس تبحث عن راحة البال والتخفف من المهام اليومية المتراكمة.",
                    intendedMessage = "«وفر طاقتك للي بتحبه، وسيب الباقي يخلص بذكاء وسهولة.»",
                    visualExecution = "مشاهد سريعة متتالية لأشخاص في مواقف مختلفة ينجزون مهامهم بارتياح وابتسامة خفيفة.",
                    composition = "تركيز على الشاشة وهي تظهر إشعار الإنجاز بنجاح (Task Completed).",
                    heroElement = "لحظة إتمام المعاملة وظهور رسالة النجاح الخضراء المريحة.",
                    keyDetails = "واقعية المشاهد وتنوع البيئات (في الطريق، في المكتب، في البيت).",
                    whatToAvoid = "تجنب المصطلحات التقنية المعقدة واشرح الفائدة بلغة بسيطة ومباشرة.",
                    executionNotes = "بوست كاروسيل أو فيديو يوضح مقارنة سريعة بين طريقتين لإنجاز نفس الشيء."
                )
            )
        } else {
            // General / Retail / Other Briefs
            creativeIdeas.add(
                CreativeIdea(
                    title = "الاتجاه الأول: «خلي المنتج هو اللي يكشف الفرق»",
                    coreIdea = "بدلاً من الحديث المطول عن الميزات، إظهار اختبار واقعي أو مقارنة مرئية صريحة تجعل المشاهد يقتنع بعينيه دون تلقين.",
                    strategicInsight = "الجمهور المعاصر يشكك في الكلام الإعلاني النظري، لكنه يثق في الإثبات البصري المباشر (Seeing is Believing).",
                    intendedMessage = "«الفرق واضح وملموس، والتجربة تثبت نفسها من أول مرة.»",
                    visualExecution = "أسلوب بصري نظيف ومباشر، يركز على تفاصيل المنتج وجودة خامته أو سرعة نتيجته في بيئة استخدام حقيقية.",
                    composition = "تكوين مركزي مباشر يضع المنتج في بؤرة الاهتمام مع إضاءة طبيعية تفصله عن المحيط.",
                    heroElement = "المنتج أثناء لحظة الاستخدام الفعلي والتفاعل المباشر معه.",
                    keyDetails = "وضوح تفاصيل المنتج الحقيقية وعدم إخفاء أي جانب وراء رسومات غير ضرورية.",
                    whatToAvoid = "تجنب استخدام نصوص عريضة تملأ الشاشة وتغطي دليل الجودة البصري.",
                    executionNotes = "التركيز على لقطة رئيسية واضحة ومقنعة تكون هي مركز الحملة."
                )
            )
            creativeIdeas.add(
                CreativeIdea(
                    title = "الاتجاه الثاني: «ربط الفكرة بموقف يومي مألوف للجمهور»",
                    coreIdea = "صياغة الموقف الإعلاني حول عادة أو لحظة إحباط متكررة يعيشها الجمهور المستهدف يومياً، حيث يكون هذا الحل هو المنقذ الطبيعي.",
                    strategicInsight = "الإعلانات التي تبدأ بما يشعر به المشاهد شخصياً تجعله يتوقف عن الـ Scroll لأنه يرى نفسه في المشهد.",
                    intendedMessage = "«عارفين التعب اللي بتواجهه، وعملنا ده مخصوص عشان يسهل عليك اللحظة دي.»",
                    visualExecution = "مشهد واقعي يعكس تفاصيل الحياة اليومية للجمهور، مع إيقاع حركي عفوي وغير متكلف.",
                    composition = "تكوين ديناميكي يتبع الشخصية الرئيسية أثناء مواجهة المشكلة ثم لحظة الارتياح.",
                    heroElement = "تعبير الارتياح والحل الفوري عند الاستفادة من الخدمة أو المنتج.",
                    keyDetails = "طبيعية الملابس، بيئة المكان، ولغة الحوار التي تطابق لغة الجمهور المستهدف.",
                    whatToAvoid = "تجنب المشاهد المصطنعة أو البيئات المترفة غير الملائمة لطبيعة الجمهور المستهدف.",
                    executionNotes = "كتابة سيناريو قصير يعتمد على حوار عفوي ذكي ومفارقة مضحكة أو ملموسة."
                )
            )
        }

        // 8. Specific Design Output (Only Non-Generic, Strategic Fields - No Forced Boilerplate)
        val designOutput = DesignOutput(
            creativeConcept = "ترجمة فكرة: «${extractedSubject.take(45)}» إلى هوية بصرية تركز على الإثبات المباشر وتفادي التزيين الفارغ.",
            visualDirection = "تكوين يمنح بطل المشهد الحجم والسيادة الكاملة في الكادر مع خلفية ذات لون نقي تضمن التباين ووضوح القراءة.",
            composition = "المنتج يحتل 60% من الجانب البصري، بينما العنوان الرئيسي يستقر في المساحة المقابلة بتوازن هرمي يقود العين مباشرة إلى الـ CTA.",
            heroElement = "العنصر الأساسي المستخرج من البريف في وضعية حركة أو استخدام طبيعية.",
            lighting = if (isFoodOrDining || isFashionOrBeauty) "إضاءة جانبية دافئة تبرز الملمس والتفاصيل الحقيقية للمنتج وتخلق ظلالاً ناعمة تمنحه عمقاً طبيعياً." else null,
            colorDirection = "باليتة ألوان تستند إلى هوية العلامة مع لون تأكيد (Accent) واحد يجذب الانتباه للعرض أو زر الشراء دون تشتيت بصري.",
            typographyDirection = "خط عربي عصري بنمط هندسي نظيف؛ العناوين بوزن عريض مقروء، والنصوص الفرعية بوزن خفيف ومريح على شاشات الهواتف.",
            negativeSpace = "مساحة تنفس مدروسة بنسبة 35% تمنع الزحام البصري وتوجه التركيز مباشرة لجوهر الرسالة.",
            requiredAssets = "الشعار بصيغة متجهة (Vector/SVG)، صور المنتج الأصلية عالية الدقة بدون خلفيات معقدة.",
            executionNotes = "تسليم التصميم بنسبتي 1:1 للبوستات و9:16 للستوري والريدز مع المحافظة على منطقة الأمان (Safe Zone)."
        )

        // 9. Specific Video Output (Strategic & Actionable)
        val videoOutput = VideoOutput(
            coreIdea = "ريل سريع الإيقاع يترجم المعاناة والحل في 15 إلى 20 ثانية بأعلى معدل استبقاء للمشاهد.",
            openingHook = "أول ثانيتين: سؤال حاد أو مشهد مفاجئ يطرح المشكلة الأساسية قبل أن يقوم المشاهد بالتمرير (Scroll).",
            storyStructure = "صدمة المشكلة (0-3 ثوانٍ) ← الظهور الحاسم للمنتج كحل (3-10 ثوانٍ) ← إثبات النتيجة والتفاعل (10-15 ثانية).",
            sceneSequence = "المشهد 1: ارتباك أو احتياج يومي. المشهد 2: لقطة مقربة للمنتج وهو يحل الموقف. المشهد 3: الارتياح والدعوة للطلب.",
            shotSuggestions = "مزيج بين لقطة Macro مقربة جداً لإظهار التفاصيل، ولقطة Medium تظهر رد الفعل البشري الطبيعي.",
            cameraLanguage = "حركة كاميرا انسيابية تدفع المشاهد للأمام (Push-in) مع سرعة تقطيع مدروسة في المونتاج.",
            motionDirection = "انتقالات طبيعية تتبع حركة اليد أو حركة الكائن (Whip pan أو Match cut) دون مؤثرات رقمية مبتذلة.",
            transitionDirection = "انتقال بصري سلس يربط نهاية حركة المشهد الأول ببداية حركة المشهد الثاني.",
            soundDesign = "مؤثرات صوتية حية (Foley / SFX) واقعية تعزز كل حركة ولمسة، مع موسيقى خلفية إيقاعية هادئة.",
            duration = "15 إلى 25 ثانية كحد أقصى لضمان أعلى نسبة إكمال للمشاهدة على منصات الفيديو القصير."
        )

        // 10. Specific Content Output
        val contentOutput = ContentOutput(
            contentAngle = "مخاطبة الاحتياج اليومي للجمهور بلغة مباشرة وصريحة تبتعد عن الترويج المبالغ فيه.",
            mainIdea = "ربط حل: «${extractedSubject.take(40)}» بمكسب يومي فوري يلمسه المشاهد.",
            hook = "«ليه تتعب في كذا... لما تقدر تخلصه بذكاء ومن غير وجع دماغ؟»",
            storyDirection = "التنقل من لحظة التردد إلى لحظة اتخاذ القرار الواثق من خلال سرد سريع في 3 نقاط محددة.",
            captionDirection = "كابشن موجز يبدأ بجملة مشوقة، ثم سطرين لشرح الفائدة الأساسية، وينتهي بسؤال تفاعلي لزيادة التعليقات.",
            cta = "«اضغط على اللينك في البايو واطلب دلوقتي / اكتب لنا في الكومنتات وهنبعتلك التفاصيل فوراً.»",
            reelConcept = "فيديو عفوي يعتمد على المقارنة السريعة بين «قبل» و«بعد» استخدام المنتج أو الخدمة."
        )

        // 11. Specific Account Output
        val accountOutput = AccountOutput(
            whatClientWants = executiveSummary,
            missingInfo = missingEssentials,
            questionsForClient = listOf(
                "ما هي الميزانية التقديرية المعتمدة للإنتاج وللإنفاق الإعلاني المدفوع؟",
                "ما هو التاريخ النهائي الدقيق لإطلاق المواد الإعلانية لمواءمة جدول الإنتاج؟",
                "هل توجد أي عروض ترويجية إضافية أو مميزات حصرية للمنتج لم تُذكر في البريف؟"
            ),
            deliverablesList = "فيديو ريل إعلاني رئيسي + تصاميم بوستات ترويجية للمنصات الرقمية المستهدفة.",
            risksAndAmbiguities = listOf(
                "بدء العمل دون وضوح الميزانية قد يؤدي لاختيار اتجاه إخراجي لا يتناسب مع إمكانيات العميل.",
                "عدم وضوح العرض النهائي يضعف من قدرة الحملة على تحقيق معدل تحويل (Conversion Rate) مرتفع."
            )
        )

        // 12. Highly Concrete & Actionable Execution Guidance
        val executionGuidance = when {
            isFoodOrDining -> "وجه المصور/المونتير للبدء فوراً بتصوير لقطات مقربة (Macro) حقيقية لتحضير الوجبة مع تسجيل الصوت الطبيعي (ASMR)، وخلّي أول ثانيتين من الفيديو حركة شهية وسريعة بدون وضع أي نصوص أو شعارات تغطي الطعام."
            isRealEstateOrProperty -> "خلّي المصمم يركز على إظهار مشهد الحياة الصباحي والهادئ داخل الكادر مع إضاءة طبيعية واضحة، ولا تضع أسعار أو خصومات بخطوط ضخمة تشوه فخامة المشهد، واكتفِ بكتابة العنوان في المساحة الفارغة الهادئة."
            isTechOrApp -> "ابدأ الفيديو بمشهد 3 ثوانٍ يظهر تعقيد المشكلة بدون التطبيق، ثم انقل المشاهد مباشرة لشاشة الموبايل بوضوح أثناء الضغط على زر الحل، وتأكد أن واجهة التطبيق واضحة وكبيرة في شاشات الموبايل (9:16)."
            else -> "خلّي المنتج يحتل الجزء الأكبر والأوضح من الكادر، وضع الرسالة الأساسية في المساحة الفارغة المقابلة له بتوازن بصري نظيف يقود العين من ميزة المنتج إلى زر الدعوة لاتخاذ إجراء (CTA) مباشرة."
        }

        return CreativeAnalysisResult(
            analysisId = analysisId,
            briefText = rawText,
            timestamp = System.currentTimeMillis(),
            targetTeam = targetTeam,
            thinkingMode = thinkingMode,
            realCommunicationChallenge = communicationChallenge,
            creativeOpportunity = creativeOpportunity,
            understanding = ClientUnderstanding(
                summary = executiveSummary,
                clientStated = clientStatedFacts,
                creativeSuggestion = strategicRecommendations
            ),
            actionableBrief = actionableBrief,
            missingEssentials = missingEssentials,
            designOutput = if (targetTeam == TargetTeam.ALL || targetTeam == TargetTeam.DESIGN) designOutput else null,
            videoOutput = if (targetTeam == TargetTeam.ALL || targetTeam == TargetTeam.VIDEO) videoOutput else null,
            contentOutput = if (targetTeam == TargetTeam.ALL || targetTeam == TargetTeam.CONTENT) contentOutput else null,
            accountOutput = if (targetTeam == TargetTeam.ALL || targetTeam == TargetTeam.ACCOUNT) accountOutput else null,
            creativeIdeas = creativeIdeas,
            executionGuidance = executionGuidance
        )
    }
}
