# Custom ROM catalogue import

The full OneOS app includes a **Custom** card in the existing system carousel. It lets the user select a local ROM file, assign a display name, keep the entry in the catalogue, inspect its file information, and remove it later.

## Important compatibility boundary

Adding a file to the catalogue does not convert it into a OneOS guest image and does not make it bootable. OneOS runs guest images through a device-specific runtime on top of the host kernel. Android version alone is not a compatibility guarantee.

A ROM can only be enabled for setup after its image format, Android release, CPU ABI, partition layout, required kernel interfaces, vendor dependencies, and OneOS guest integration have been checked for the target phone. The current custom catalogue entries are intentionally marked **NOT VALIDATED** and cannot enter the setup or launch path.

## Android 17 goal

The project aims to accept as many Android 17-based systems as can be validated, including AOSP-derived and vendor-derived ROMs. This is a compatibility target, not a promise that every Android 17 ROM works. Device-specific ports, images requiring a different kernel, unsupported dynamic partitions, incompatible vendor interfaces, and ROMs that assume a normal boot chain may not be usable as guests.

## Current import behavior

- Select a file through Android's document picker.
- Keep a persisted read permission to that file.
- Store a display name and file metadata in the app's local catalogue.
- Show a warning that compatibility is not verified.
- Remove an imported entry without modifying the source file.

No flashing, bootloader modification, partition writing, or automatic ROM conversion is performed by this catalogue importer.
