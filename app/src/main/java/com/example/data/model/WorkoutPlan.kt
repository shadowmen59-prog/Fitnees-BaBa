package com.example.data.model

data class Exercise(
    val name: String,
    val repsDescription: String,
    val targetMuscle: String,
    val description: String
)

data class WorkoutPlan(
    val id: String,
    val name: String,
    val gender: String, // "Male" or "Female"
    val type: String, // "Strength", "Cardio", "Recovery"
    val description: String,
    val estimatedMinutes: Int,
    val exercises: List<Exercise>
)

object PresetPlans {
    val malePlans = listOf(
        WorkoutPlan(
            id = "male_strength_push",
            name = "Push Day (Hypertrophy & Power)",
            gender = "Male",
            type = "Strength",
            description = "A powerful workout focusing on chest, shoulders, and triceps build-up. Perfect for increasing pushing strength and lean mass.",
            estimatedMinutes = 55,
            exercises = listOf(
                Exercise("Barbell Bench Press", "4 sets x 8-12 reps", "Chest", "Lie flat on a bench, grip the barbell slightly wider than shoulder width, lower it to your chest, then press upward with power."),
                Exercise("Overhead Barbell Press", "4 sets x 8-10 reps", "Shoulders", "Stand tall with core tight, bar at upper chest level. Press the bar overhead until arms are locked out, keeping spine stable."),
                Exercise("Incline Dumbbell Flyes", "3 sets x 12 reps", "Upper Chest", "Set incline bench to 30 degrees. Hold dumbbells with neutral grip, flare arms slightly out, and squeeze chest at the top."),
                Exercise("Tricep Dips (Weighted/Body)", "3 sets x 10-15 reps", "Triceps", "Lower yourself on parallel bars until elbows are at 90 degrees, then push through triceps to initial lockout position."),
                Exercise("Lateral Dumbbell Raises", "4 sets x 15 reps", "Lateral Delts", "Stand straight, raise dumbbells out to your sides until parallel to the floor, leading with the elbows.")
            )
        ),
        WorkoutPlan(
            id = "male_strength_pull",
            name = "Pull Day (V-Taper & Core)",
            gender = "Male",
            type = "Strength",
            description = "Focuses heavily on developing back thickness, width, primary bicep activation, and posture alignment.",
            estimatedMinutes = 60,
            exercises = listOf(
                Exercise("Barbell Deadlift", "4 sets x 5 reps", "Lower Back & Hamstrings", "Stand over the bar, grip overhand, hinge hips, and lift by pushing the earth away. Keep a flat spine throughout."),
                Exercise("Wide-Grip Pull-Ups", "4 sets to Failure", "Lats", "Hang from a pull-up bar, draw elbows down and back to lift your upper chest to the bar, engaging your lats."),
                Exercise("Bent-Over Barbell Rows", "3 sets x 8-12 reps", "Upper & Mid Back", "Hinge forward at 45 degrees, pull barbell to lower chest while squeezing shoulder blades together tightly."),
                Exercise("Incline Hammer Curls", "3 sets x 12 reps", "Biceps & Brachialis", "Sit on 45-degree incline bench with dumbbells, curl with neutral palms-in grip to emphasize biceps brachii."),
                Exercise("Face Pulls (with Rope Connect)", "4 sets x 15 reps", "Rear Delts & Rotator Cuff", "Pull cable rope toward your face while flaring elbows high and out, rotating wrists backward at end of motion.")
            )
        ),
        WorkoutPlan(
            id = "male_strength_legs",
            name = "Leg Day (Quad / Ham Power)",
            gender = "Male",
            type = "Strength",
            description = "Intense leg progression designed for overall lower-body testosterone release and functional athletic leap potential.",
            estimatedMinutes = 50,
            exercises = listOf(
                Exercise("Barbell Back Squats", "4 sets x 6-10 reps", "Quads & Glutes", "Rest bar on upper traps, descend into deep squat past parallel, then drive heels into the floor to return up."),
                Exercise("Romanian Deadlifts", "3 sets x 10-12 reps", "Hamstrings", "Grip bar overhead, hinge back at hips with soft knees, feel deep hamstring stretch, then pull hips forward."),
                Exercise("Leg Press (High Stance)", "3 sets x 12-15 reps", "Quads & Calves", "Place feet shoulder-width on sled, release safety, lower weight slowly to 90 degrees knee extension, and press up."),
                Exercise("Standing Calf Raises", "4 sets x 15-20 reps", "Calves", "On clean block, lower heels to full stretch, then press to tiptoes for a hard squeezy peak contraction."),
                Exercise("Hanging Leg Raises", "3 sets x 12-15 reps", "Lower Abs", "Hang from bar, raise legs up straight to 90 degrees using lower abdominal strength without excessive swinging.")
            )
        ),
        WorkoutPlan(
            id = "male_cardio_hiit",
            name = "Alpha HIIT Conditioning",
            gender = "Male",
            type = "Cardio",
            description = "High-energy functional circuit designed to maximize dynamic lung capacity and boost active lipolysis (fat burn).",
            estimatedMinutes = 35,
            exercises = listOf(
                Exercise("Battle Rope Slams", "5 rounds x 45s work / 15s rest", "Upper Body & Conditioning", "Slam ropes down forcefully with alternating and double waves as fast as possible utilizing hip drive."),
                Exercise("Heavy Kettlebell Swings", "4 sets x 20 reps", "Full Body & Posterior Chain", "Hinge hips with kettlebell between legs, drive hips forward aggressively to swing kettlebell to shoulder level."),
                Exercise("Rowing Machine (HIIT intervals)", "10 intervals x 30s sprint / 30s light", "Full Body Cardio", "Pull handle with maximum strokes, driving off leg pads, and return smooth during active recovery intervals."),
                Exercise("Sprint Intervals", "6 repetitions x 100m sprint", "Quads & Conditioning", "Perform raw maximum-speed sprint on treadmill or track, followed by 1 min walk active rest.")
            )
        ),
        WorkoutPlan(
            id = "male_recovery_mobility",
            name = "Deep Tissue & Joints Restore",
            gender = "Male",
            type = "Recovery",
            description = "A strategic decompression routine to prevent injury, roll out tight muscle fibers, and restore range of motion.",
            estimatedMinutes = 20,
            exercises = listOf(
                Exercise("Foam Rolling (Quads & Lats)", "5 minutes active roll", "Full Body Fascia", "Slowly roll tight areas over a high-density foam roller. Pause on tender knots for 20-30 seconds to release tension."),
                Exercise("Deep Squat Hold (Mobility)", "2 holds x 60s", "Hips & Ankles", "Sit deep in a squat bodyweight file, elbows pressing knees out, keeping chest upright and feet glued flat."),
                Exercise("Banded Hamstring Stretch", "2 sets x 45s per leg", "Hamstrings", "Lie on back, loop band around foot, pull leg upward vertically while keeping knee straight to lengthen the muscle."),
                Exercise("Cobra Pose (Spinal extension)", "3 repetitions x 30s hold", "Spine & Core", "Lie face down, place hands under shoulders, press up gently to arches of lower back, feeling core stretch.")
            )
        )
    )

    val femalePlans = listOf(
        WorkoutPlan(
            id = "female_strength_glutes",
            name = "Glute Emphasis & Posterior Power",
            gender = "Female",
            type = "Strength",
            description = "An extensive lower-body sculpt focusing on maximum glute hypertrophy, hamstring definition, and core stability.",
            estimatedMinutes = 55,
            exercises = listOf(
                Exercise("Barbell Hip Thrusts", "4 sets x 10-12 reps", "Glutes", "Place upper back on bench, barbell over hips. Drive hips up, squeeze glutes at peak lockout for a full second, and lower."),
                Exercise("Dumbbell Romanian Deadlifts", "4 sets x 10-12 reps", "Hamstrings & Glutes", "Hold heavy dumbbells in front of thighs, push hips back with flat back, and curl hips back up using glutes."),
                Exercise("Bulgarian Split Squats", "3 sets x 8-10 reps per leg", "Quads & Glutes", "Elevate rear foot on bench, lower hips until front thigh is parallel, then push through front heel firmly."),
                Exercise("Cable Glute Kickbacks", "3 sets x 15 reps per leg", "Glutes", "Attach ankle strap, bend over cable machine, and kick leg backward and slightly upward in a controlled arc.")
            )
        ),
        WorkoutPlan(
            id = "female_strength_upper",
            name = "Upper Body Sculpt & Core Define",
            gender = "Female",
            type = "Strength",
            description = "Focuses on building elegant posture, toning the shoulders, lifting the chest, and defining the abdominal wall.",
            estimatedMinutes = 45,
            exercises = listOf(
                Exercise("Lat Pulldown (Wide Grip)", "4 sets x 10-12 reps", "Lats & Back", "Grip wide bar, pull downward to collarbones by driving elbows down. Release slowly under control."),
                Exercise("Dumbbell Shoulder Press", "3 sets x 10-12 reps", "Shoulders", "Sit straight, press dumbbells from shoulder level upwards until fully extended overhead without arching lower back."),
                Exercise("Incline Dumbbell Curl", "3 sets x 12 reps", "Biceps", "Set bench to 45 degrees. Allow arms to hang, then curl dumbbells towards shoulders while keeping elbows pinned back."),
                Exercise("Push-ups (Full or Incline)", "3 sets to max reps", "Chest & Core", "Keep body in strict straight-line plank, lower chest to floor, and push back up. Elevate hands if needed."),
                Exercise("Plank Shoulder Taps", "3 sets x 30s", "Transverse Abdominis", "Hold a solid push-up plank, and tap alternate shoulders with hands while keeping hips completely still.")
            )
        ),
        WorkoutPlan(
            id = "female_strength_quads",
            name = "Quad & Hamstring Definition",
            gender = "Female",
            type = "Strength",
            description = "A well-rounded leg development day that avoids overbuilding the waist while developing athletic leg muscles.",
            estimatedMinutes = 50,
            exercises = listOf(
                Exercise("Goblet Squats (Deep)", "4 sets x 10-12 reps", "Quads & Glutes", "Hold a dumbbell vertically against chest. Squat deeply between your knees while keeping torso highly upright."),
                Exercise("Leg Press (Wide-Stance)", "3 sets x 12-15 reps", "Inside Thighs/Adductors", "Sit open, place feet wide and high on sled to activate inner thighs and hamstrings, lowering deeply."),
                Exercise("Lying Hamstring Curls", "3 sets x 12-15 reps", "Hamstrings", "Lie face down, align knee caps with machine pivot, and curl pad towards glutes; slow releases."),
                Exercise("Dumbbell Walking Lunges", "3 sets x 20 steps total", "Quads & Glutes", "Take dynamic forward strides, bending back knee until almost touching floor, driving upward into next step.")
            )
        ),
        WorkoutPlan(
            id = "female_cardio_intervals",
            name = "Lean Burn Interval Training",
            gender = "Female",
            type = "Cardio",
            description = "A calorie-melting aerobic routine targeting cardiorespiratory endurance and boosting active metabolism hours after.",
            estimatedMinutes = 40,
            exercises = listOf(
                Exercise("Incline Treadmill Power Walk", "15 minutes climb", "Posterior Burn & Endurance", "Set treadmill to 10% incline at 3.5 mph. Pump arms, drive hips, and step strictly with heels to fire glutes."),
                Exercise("Stairmaster Intermittent climb", "15 minutes routine", "Glutes & Lungs", "Perform 1 min speed 8, 1 min speed 4 alternating climbs. Take uniform strides without holding rails."),
                Exercise("Rope Jumps (Single under)", "5 sets x 1 min work", "Calves & Conditioning", "Perform smooth, rapid jump-rope cycles holding elbows near hips, jumping softly on balls of feet."),
                Exercise("Mountain Climbers", "4 sets x 40s fast focus", "Core Conditioning", "Hold a plank, drive knees rapidly to chest alternating feet without bouncing hips upward.")
            )
        ),
        WorkoutPlan(
            id = "female_recovery_flow",
            name = "Aesthetic Alignment & Flexibility",
            gender = "Female",
            type = "Recovery",
            description = "A deep yoga-inspired stretching routing targeting lower back relief, hip opening, and graceful spine extension.",
            estimatedMinutes = 20,
            exercises = listOf(
                Exercise("Child's Pose Decompression", "2 sets x 60s hold", "Shoulders & Lower Back", "Sit back on heels, stretch arms forward on mat, drop forehead down and breathe deeply into upper ribs."),
                Exercise("Pigeon Stretch (Hip Opener)", "2 sets x 45s per side", "Gluteus Medius & Piriformis", "Bring forward leg folded across mat, stretch rear leg long. Lower torso over folded knee for a beautiful release."),
                Exercise("Cat-Camel Mobility Wave", "10 slow cycles", "Thoracic Spine Flex", "Arch your back up highly like a cat, then drop stomach, lift chin like a camel. Keep movements soft and flowing."),
                Exercise("Butterfly Stretch (Hip opener)", "2 sets x 60s hold", "Adductors/Inner Thigh", "Sit tall, pull soles of feet together, let knees fall open. Gently press thighs down utilizing elbows.")
            )
        )
    )

    fun getPlansFor(gender: String): List<WorkoutPlan> {
        return if (gender.lowercase() == "female") femalePlans else malePlans
    }
}
